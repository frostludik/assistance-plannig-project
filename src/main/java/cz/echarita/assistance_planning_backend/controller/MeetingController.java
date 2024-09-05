package cz.echarita.assistance_planning_backend.controller;

import cz.echarita.assistance_planning_backend.exception.MeetingException;
import cz.echarita.assistance_planning_backend.model.Meeting;
import cz.echarita.assistance_planning_backend.service.MeetingService;
import cz.echarita.assistance_planning_backend.utilities.ValidationUtils;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/meetings")
@Slf4j
@Tag(name = "Meetings", description = "APIs for managing meetings")
public class MeetingController {

    private final MeetingService meetingService;

    @Autowired
    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @GetMapping
    @Operation(summary = "Get all meetings", description = "Fetch all meetings from the database")
    public ResponseEntity<?> getAllMeetings() {
        log.info("Fetching all meetings");
        try {
            List<Meeting> meetings = meetingService.getAllMeetings();
            log.info("Successfully fetched all meetings");
            return new ResponseEntity<>(meetings, HttpStatus.OK);
        } catch (MeetingException ex) {
            log.error("Error fetching meetings: {}", ex.getMessage());
            if (ex.getStatus() == HttpStatus.NOT_FOUND) {
                return new ResponseEntity<>("No meetings found", ex.getStatus());
            }
            return new ResponseEntity<>(Collections.emptyList(), ex.getStatus());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get meeting by ID", description = "Fetch a meeting by its ID")
    public ResponseEntity<?> getMeetingById(
            @Parameter(description = "ID of the meeting") @PathVariable Long id) {
        log.info("Fetching meeting with ID: {}", id);
        try {
            Meeting meeting = meetingService.getMeetingById(id);
            log.info("Successfully fetched meeting with ID: {}", id);
            return new ResponseEntity<>(meeting, HttpStatus.OK);
        } catch (MeetingException ex) {
            log.error("Error fetching meeting with ID {}: {}", id, ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
        }
    }

    @PostMapping
    @Operation(summary = "Create a new meeting", description = "Create a new meeting with the provided details")
    public ResponseEntity<?> createMeeting(@RequestBody Meeting meeting) {
        log.info("Creating meeting: {}", meeting);
        try {
            if (!ValidationUtils.isValidMeetingTime(meeting)) {
                log.warn("Invalid meeting times for meeting: {}", meeting);
                throw new MeetingException("Invalid or missing start and/or end times. Start time cannot be later than end", HttpStatus.BAD_REQUEST);
            }
            List<Meeting> allMeetings = meetingService.getAllMeetingsWithoutException();
            if (ValidationUtils.isOverlapping(meeting, allMeetings)) {
                log.warn("Meeting overlaps with existing meeting: {}", meeting);
                throw new MeetingException("Meeting overlaps with existing meeting", HttpStatus.BAD_REQUEST);
            }
            Meeting createdMeeting = meetingService.createMeeting(meeting);
            log.info("Successfully created meeting: {}", createdMeeting);
            return new ResponseEntity<>(createdMeeting, HttpStatus.CREATED);
        } catch (MeetingException ex) {
            log.error("Error creating meeting: {}", ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a meeting", description = "Update an existing meeting by its ID")
    public ResponseEntity<?> updateMeeting(
            @Parameter(description = "ID of the meeting") @PathVariable Long id,
            @RequestBody Meeting updatedMeeting) {
        log.info("Updating meeting with ID: {}", id);
        try {
            Meeting meeting = meetingService.updateMeeting(id, updatedMeeting);
            log.info("Successfully updated meeting with ID: {}", id);
            return new ResponseEntity<>(meeting, HttpStatus.OK);
        } catch (MeetingException ex) {
            log.error("Error updating meeting with ID {}: {}", id, ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
        }
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a meeting", description = "Delete a meeting by its ID")
    public ResponseEntity<String> deleteMeeting(
            @Parameter(description = "ID of the meeting") @PathVariable Long id) {
        log.info("Deleting meeting with ID: {}", id);
        try {
            meetingService.deleteMeeting(id);
            log.info("Successfully deleted meeting with ID: {}", id);
            return new ResponseEntity<>("Meeting with id " + id + " successfully deleted.", HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Error deleting meeting with ID {}: {}. Meeting not found", id, ex.getMessage());
            return new ResponseEntity<>("Meeting not found with id: " + id, HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/assist/{assistID}")
    @Operation(summary = "Get meetings by assist ID", description = "Fetch meetings by assist ID, optionally within a date range")
    public ResponseEntity<?> getMeetingsByAssist(
            @Parameter(description = "ID of the assist") @PathVariable String assistID,
            @Parameter(description = "Start date for the range") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate since,
            @Parameter(description = "End date for the range") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate until) {
        log.info("Fetching meetings for assist ID: {} from {} to {}", assistID, since, until);
        try {
            List<Meeting> meetings;

            if (since != null && until != null) {
                LocalDateTime sinceDateTime = since.atStartOfDay();
                LocalDateTime untilDateTime = until.plusDays(1).atStartOfDay();
                meetings = meetingService.getMeetingsByAssistInTimeRange(assistID, sinceDateTime, untilDateTime);
            } else {
                meetings = meetingService.getMeetingsByAssist(assistID);
            }
            log.info("Successfully fetched meetings for assist ID: {}", assistID);
            return new ResponseEntity<>(meetings, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            log.error("Invalid input parameters for assist ID {}: {}", assistID, ex.getMessage());
            return new ResponseEntity<>("Invalid input parameters: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            log.error("Failed to retrieve meetings for assist ID {}: {}", assistID, ex.getMessage());
            return new ResponseEntity<>("Failed to retrieve meetings: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





    @GetMapping("/client/{clientID}")
    @Operation(summary = "Get meetings by client ID", description = "Fetch meetings by client ID, optionally within a date range")
    public ResponseEntity<?> getMeetingsByClient(
            @Parameter(description = "ID of the client") @PathVariable String clientID,
            @Parameter(description = "Start date for the range") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate since,
            @Parameter(description = "End date for the range") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate until) {
        log.info("Fetching meetings for client ID: {} from {} to {}", clientID, since, until);
        try {
            List<Meeting> meetings;

            if (since != null && until != null) {
                LocalDateTime sinceDateTime = since.atStartOfDay();
                LocalDateTime untilDateTime = until.plusDays(1).atStartOfDay();
                meetings = meetingService.getMeetingsByClientInTimeRange(clientID, sinceDateTime, untilDateTime);
            } else {
                meetings = meetingService.getMeetingsByClient(clientID);
            }
            log.info("Successfully fetched meetings for client ID: {}", clientID);
            return new ResponseEntity<>(meetings, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            log.error("Invalid input parameters for client ID {}: {}", clientID, ex.getMessage());
            return new ResponseEntity<>("Invalid input parameters: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            log.error("Failed to retrieve meetings for client ID {}: {}", clientID, ex.getMessage());
            return new ResponseEntity<>("Failed to retrieve meetings: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
