package cz.echarita.assistance_planning_backend.controller;

import cz.echarita.assistance_planning_backend.controller.dto.MeetingPlanRequestDTO;
import cz.echarita.assistance_planning_backend.controller.dto.MeetingPlanResponseDTO;
import cz.echarita.assistance_planning_backend.controller.dto.PredefinedMeetingDTO;
import cz.echarita.assistance_planning_backend.controller.dto.UpdateCustomerActiveStatusRequestDTO;
import cz.echarita.assistance_planning_backend.controller.dto.UpdateCustomerActiveStatusResponseDTO;
import cz.echarita.assistance_planning_backend.controller.dto.UpdateValidUntilResponseDTO;
import cz.echarita.assistance_planning_backend.exception.ResourceNotFoundException;
import cz.echarita.assistance_planning_backend.model.Customer;
import cz.echarita.assistance_planning_backend.model.PredefinedMeeting;
import cz.echarita.assistance_planning_backend.service.CustomerService;
import cz.echarita.assistance_planning_backend.service.PredefinedMeetingService;
import cz.echarita.assistance_planning_backend.service.WeekService;
import cz.echarita.assistance_planning_backend.utilities.DateUtils;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/define/meeting")
@Slf4j
@Tag(name = "Predefined Meetings", description = "APIs for managing predefined meetings")
public class PredefinedMeetingController {

  @Autowired
  private PredefinedMeetingService predefinedMeetingService;

  @Autowired
  private CustomerService customerService;

  @Autowired
  private WeekService weekService;

  @PostMapping("/{customerID}")
  @CrossOrigin
  @Operation(summary = "Define a meeting", description = "Define a new predefined meeting for a customer")
  public ResponseEntity<?> defineMeeting(
          @Parameter(description = "ID of the customer") @PathVariable Long customerID,
          @RequestBody PredefinedMeetingDTO predefinedMeetingDTO) {
    log.info("Defining meeting for customer ID: {} with details: {}", customerID, predefinedMeetingDTO);
    PredefinedMeeting createdMeeting;
    try {
      createdMeeting = predefinedMeetingService.defineMeeting(predefinedMeetingDTO);
      log.info("Successfully defined meeting for customer ID: {}", customerID);
    } catch (Exception e) {
      log.error("Error defining meeting for customer ID {}: {}", customerID, e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
    PredefinedMeetingDTO responseDTO = predefinedMeetingService.toDTO(createdMeeting);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
  }

  @GetMapping("/{customerID}")
  @Operation(summary = "Get meetings by customer ID", description = "Fetch predefined meetings by customer ID")
  public ResponseEntity<List<PredefinedMeetingDTO>> getMeetingsByCustomerID(
          @Parameter(description = "ID of the customer") @PathVariable Long customerID) {
    log.info("Fetching meetings for customer ID: {}", customerID);
    try {
      List<PredefinedMeetingDTO> meetings = predefinedMeetingService.getMeetingsByCustomerIDs(
              List.of(customerID));
      log.info("Successfully fetched meetings for customer ID: {}", customerID);
      return ResponseEntity.ok(meetings);
    } catch (Exception ex) {
      log.error("Error fetching meetings for customer ID {}: {}", customerID, ex.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
    }
  }


  @GetMapping("/customerIDs")
  @Operation(summary = "Get meetings by list of customer IDs", description = "Fetch predefined meetings by a list of customer IDs")
  public ResponseEntity<List<PredefinedMeetingDTO>> getMeetingsByListOfCustomerIDs(
          @Parameter(description = "List of customer IDs") @RequestParam List<String> ids) {
    log.info("Fetching meetings for customer IDs: {}", ids);
    try {
      List<Long> customerIDList = ids.stream()
              .map(Long::parseLong)
              .collect(Collectors.toList());
      List<PredefinedMeetingDTO> meetings = predefinedMeetingService.getMeetingsByCustomerIDs(
              customerIDList);
      log.info("Successfully fetched meetings for customer IDs: {}", ids);
      return ResponseEntity.ok(meetings);
    } catch (Exception ex) {
      log.error("Error fetching meetings for customer IDs {}: {}", ids, ex.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
    }
  }

  @GetMapping("/{customerID}/valid")
  @Operation(summary = "Get meetings for a week", description = "Fetch predefined meetings for a customer for a specific calendar week and year")
  public ResponseEntity<List<PredefinedMeetingDTO>> getMeetingsForWeek(
          @Parameter(description = "ID of the customer") @PathVariable Long customerID,
          @Parameter(description = "Calendar week") @RequestParam int cw,
          @Parameter(description = "Year") @RequestParam int year) {

    log.info("Fetching meetings for customer ID: {} for calendar week: {} and year: {}", customerID, cw, year);
    try {
      LocalDate startDate = DateUtils.getStartDate(cw, year);
      LocalDate endDate = DateUtils.getEndDate(cw, year);

      List<PredefinedMeetingDTO> meetings = predefinedMeetingService.getMeetingsForWeek(customerID, startDate, endDate);
      log.info("Successfully fetched meetings for customer ID: {} for calendar week: {} and year: {}", customerID, cw, year);
      return ResponseEntity.ok(meetings);
    } catch (Exception ex) {
      log.error("Error fetching meetings for customer ID {}: {}", customerID, ex.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
    }
  }

  @PostMapping("/planned")
  @Operation(summary = "Get planned hours", description = "Fetch the planned hours for predefined meetings based on the provided request")
  public ResponseEntity<MeetingPlanResponseDTO> getPlannedHours(
          @RequestBody MeetingPlanRequestDTO request) {
    log.info("Fetching planned hours with request: {}", request);
    try {
      MeetingPlanResponseDTO response = predefinedMeetingService.getPlannedHours(request);
      log.info("Successfully fetched planned hours for request: {}", request);
      return ResponseEntity.ok(response);
    } catch (Exception ex) {
      log.error("Error fetching planned hours for request {}: {}", request, ex.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @PatchMapping("/{ID}")
  @Operation(summary = "Update a meeting", description = "Update an existing predefined meeting by its ID")
  public ResponseEntity<?> updateMeeting(
          @Parameter(description = "ID of the meeting") @PathVariable Long ID,
          @RequestBody UpdateValidUntilResponseDTO updateRequest) {

    log.info("Updating meeting with ID: {} with request: {}", ID, updateRequest);
    try {
      UpdateValidUntilResponseDTO updatedMeeting = predefinedMeetingService.updateMeeting(ID, updateRequest);
      log.info("Successfully updated meeting with ID: {}", ID);
      return ResponseEntity.ok(updatedMeeting);
    } catch (ResourceNotFoundException e) {
      log.error("Error updating meeting with ID {}: {}. Meeting not found.", ID, e.getMessage());
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
  }


  @PatchMapping("/customer/{customerID}/active")
  @Operation(summary = "Update customer active status", description = "Update the active status of a customer by their ID")
  public ResponseEntity<?> updateCustomerActiveStatus(
          @Parameter(description = "ID of the customer") @PathVariable Long customerID,
          @RequestBody UpdateCustomerActiveStatusRequestDTO request) {

    log.info("Updating active status for customer ID: {} with request: {}. Current status: {}", customerID, request, request.getIsActive() ? "active" : "inactive");
    try {
      Customer updatedCustomer = customerService.updateCustomerActiveStatus(customerID, request);
      log.info("Successfully updated active status for customer ID: {}. New status: {}", customerID, updatedCustomer.isActive() ? "active" : "inactive");

      UpdateCustomerActiveStatusResponseDTO response = new UpdateCustomerActiveStatusResponseDTO(
              updatedCustomer.getCustomerID(),
              updatedCustomer.isActive());
      return ResponseEntity.ok(response);
    } catch (Exception ex) {
      log.error("Error updating active status for customer ID {}: {}", customerID, ex.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
  }

}
