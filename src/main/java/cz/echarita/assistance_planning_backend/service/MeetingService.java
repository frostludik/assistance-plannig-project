package cz.echarita.assistance_planning_backend.service;

import cz.echarita.assistance_planning_backend.exception.MeetingException;
import cz.echarita.assistance_planning_backend.model.Meeting;
import cz.echarita.assistance_planning_backend.repository.MeetingRepository;
import cz.echarita.assistance_planning_backend.utilities.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
public class MeetingService {

    private final MeetingRepository meetingRepository;

    @Autowired
    public MeetingService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public List<Meeting> getAllMeetings() {
        log.info("Fetching all meetings");
        List<Meeting> meetings = meetingRepository.findAll();
        log.info("Successfully fetched all meetings");
        return meetings;
    }

    public List<Meeting> getAllMeetingsWithoutException() {
        log.info("Fetching all meetings without exception");
        List<Meeting> meetings = meetingRepository.findAll();
        log.info("Successfully fetched all meetings without exception");
        return meetings;
    }

    public Meeting getMeetingById(Long id) {
        log.info("Fetching meeting with ID: {}", id);
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(() -> new MeetingException("Meeting not found with id: " + id, HttpStatus.NOT_FOUND));
        log.info("Successfully fetched meeting with ID: {}", id);
        return meeting;
    }

    public Meeting createMeeting(Meeting meeting) {
        log.info("Creating meeting: {}", meeting);
        if (!ValidationUtils.isValidMeetingTime(meeting)) {
            log.warn("Invalid meeting times for meeting: {}", meeting);
            throw new MeetingException("Invalid or missing start and/or end times. Start time cannot be later than end", HttpStatus.BAD_REQUEST);
        }
        List<Meeting> allMeetings = meetingRepository.findAll();
        if (ValidationUtils.isOverlapping(meeting, allMeetings)) {
            log.warn("Meeting overlaps with existing meeting: {}", meeting);
            throw new MeetingException("Meeting overlaps with existing meeting.", HttpStatus.CONFLICT);
        }
        Meeting createdMeeting = meetingRepository.save(meeting);
        log.info("Successfully created meeting: {}", createdMeeting);
        return createdMeeting;
    }

    public Meeting updateMeeting(Long id, Meeting updatedMeeting) {
        log.info("Updating meeting with ID: {}", id);
        Meeting existingMeeting = meetingRepository.findById(id)
                .orElseThrow(() -> new MeetingException("Meeting not found with id: " + id, HttpStatus.NOT_FOUND));

        existingMeeting.setAssistID(updatedMeeting.getAssistID());
        existingMeeting.setClientID(updatedMeeting.getClientID());
        existingMeeting.setStartDateTime(updatedMeeting.getStartDateTime());
        existingMeeting.setEndDateTime(updatedMeeting.getEndDateTime());
        existingMeeting.setComment(updatedMeeting.getComment());

        if (!ValidationUtils.isValidMeetingTime(existingMeeting)) {
            log.warn("Invalid meeting times for meeting with ID: {}", id);
            throw new MeetingException("Invalid or missing meeting start and end times. End time must be after start time.", HttpStatus.BAD_REQUEST);
        }
        List<Meeting> allMeetingsExceptCurrent = meetingRepository.findAllExceptId(id);
        if (ValidationUtils.isOverlapping(existingMeeting, allMeetingsExceptCurrent)) {
            log.warn("Meeting overlaps with existing meeting with ID: {}", id);
            throw new MeetingException("Meeting overlaps with existing meeting.", HttpStatus.BAD_REQUEST);
        }

        Meeting updatedMeetingResult = meetingRepository.save(existingMeeting);
        log.info("Successfully updated meeting with ID: {}", id);
        return updatedMeetingResult;
    }

    public void deleteMeeting(Long id) {
        log.info("Deleting meeting with ID: {}", id);
        if (!meetingRepository.existsById(id)) {
            log.error("Meeting not found with ID: {}", id);
            throw new MeetingException("Meeting not found with id: " + id, HttpStatus.NOT_FOUND);
        }
        meetingRepository.deleteById(id);
        log.info("Successfully deleted meeting with ID: {}", id);
    }

    public List<Meeting> getMeetingsByAssist(String assistId) {
        log.info("Fetching meetings for assist ID: {}", assistId);
        List<Meeting> meetings = meetingRepository.findByAssistID(assistId);
        log.info("Successfully fetched meetings for assist ID: {}", assistId);
        return meetings;
    }

    public List<Meeting> getMeetingsByAssistInTimeRange(String assistID, LocalDateTime since, LocalDateTime until) {
        log.info("Fetching meetings for assist ID: {} from {} to {}", assistID, since, until);
        List<Meeting> meetings = meetingRepository.findByAssistIDAndStartDateTimeBetween(assistID, since, until);
        log.info("Successfully fetched meetings for assist ID: {} from {} to {}", assistID, since, until);
        return meetings;
    }

    public List<Meeting> getMeetingsByClient(String clientID) {
        log.info("Fetching meetings for client ID: {}", clientID);
        List<Meeting> meetings = meetingRepository.findByClientID(clientID);
        log.info("Successfully fetched meetings for client ID: {}", clientID);
        return meetings;
    }

    public List<Meeting> getMeetingsByClientInTimeRange(String clientID, LocalDateTime since, LocalDateTime until) {
        log.info("Fetching meetings for client ID: {} from {} to {}", clientID, since, until);
        List<Meeting> meetings = meetingRepository.findByClientIDAndStartDateTimeBetween(clientID, since, until);
        log.info("Successfully fetched meetings for client ID: {} from {} to {}", clientID, since, until);
        return meetings;
    }
}
