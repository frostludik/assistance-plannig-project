package cz.echarita.assistance_planning_backend.service;


import cz.echarita.assistance_planning_backend.controller.dto.MeetingPlanRequestDTO;
import cz.echarita.assistance_planning_backend.controller.dto.MeetingPlanResponseDTO;
import cz.echarita.assistance_planning_backend.controller.dto.PredefinedMeetingDTO;
import cz.echarita.assistance_planning_backend.controller.dto.UpdateCustomerActiveStatusRequestDTO;
import cz.echarita.assistance_planning_backend.controller.dto.UpdateValidUntilResponseDTO;
import cz.echarita.assistance_planning_backend.exception.ResourceNotFoundException;
import cz.echarita.assistance_planning_backend.model.Customer;
import cz.echarita.assistance_planning_backend.model.PredefinedMeeting;
import cz.echarita.assistance_planning_backend.model.Week;
import cz.echarita.assistance_planning_backend.repository.CustomerRepository;
import cz.echarita.assistance_planning_backend.repository.PredefinedMeetingRepository;
import cz.echarita.assistance_planning_backend.repository.WeekRepository;
import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class PredefinedMeetingService {

  @Autowired
  private PredefinedMeetingRepository predefinedMeetingRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private WeekRepository weekRepository;

  public PredefinedMeeting defineMeeting(PredefinedMeetingDTO predefinedMeetingDTO) {
    log.info("Defining meeting for customer with sheetCustomerID: {}", predefinedMeetingDTO.getSheetCustomerID());
    Customer customer = customerRepository.findBySheetCustomerID(predefinedMeetingDTO.getSheetCustomerID())
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

    Week week = weekRepository.findById(predefinedMeetingDTO.getWeekID())
            .orElseThrow(() -> new ResourceNotFoundException("Week not found"));

    PredefinedMeeting meeting = new PredefinedMeeting();
    meeting.setCustomer(customer);
    meeting.setWeek(week);
    meeting.setValidFrom(predefinedMeetingDTO.getValidFrom());
    meeting.setValidUntil(predefinedMeetingDTO.getValidUntil());
    meeting.setStartTime(predefinedMeetingDTO.getStartTime());
    meeting.setEndTime(predefinedMeetingDTO.getEndTime());

    PredefinedMeeting savedMeeting = predefinedMeetingRepository.save(meeting);
    log.info("Successfully defined meeting for customer with sheetCustomerID: {}", predefinedMeetingDTO.getSheetCustomerID());
    return savedMeeting;
  }

  public List<PredefinedMeetingDTO> getMeetingsByCustomerIDs(List<Long> customerIDs) {
    log.info("Fetching meetings for customer IDs: {}", customerIDs);
    List<PredefinedMeeting> meetings = predefinedMeetingRepository.findAllMeetingsByCustomer_CustomerIDIn(customerIDs);
    List<PredefinedMeetingDTO> meetingDTOs = meetings.stream().map(this::toDTO).collect(Collectors.toList());
    log.info("Successfully fetched {} meetings for customer IDs: {}", meetingDTOs.size(), customerIDs);
    return meetingDTOs;
  }

  public List<PredefinedMeetingDTO> getMeetingsForWeek(Long customerID, LocalDate startDate, LocalDate endDate) {
    log.info("Fetching meetings for customer ID: {} from {} to {}", customerID, startDate, endDate);
    Customer customer = customerRepository.findById(customerID)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

    List<PredefinedMeeting> meetings = predefinedMeetingRepository.findAllByCustomer_CustomerIDAndValidFromBetween(customerID, startDate, endDate);
    List<PredefinedMeetingDTO> meetingDTOs = meetings.stream().map(this::toDTO).collect(Collectors.toList());
    log.info("Successfully fetched {} meetings for customer ID: {} from {} to {}", meetingDTOs.size(), customerID, startDate, endDate);
    return meetingDTOs;
  }

  public MeetingPlanResponseDTO getPlannedHours(MeetingPlanRequestDTO request) {
    log.info("Fetching planned hours with request: {}", request);
    Date date = request.getDate();
    List<Long> customerIDs = request.getCustomerIDs();

    LocalDate localDate = date.toLocalDate();
    LocalDate startOfWeek = localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    LocalDate endOfWeek = localDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

    Date startDate = Date.valueOf(startOfWeek);
    Date endDate = Date.valueOf(endOfWeek);

    List<Customer> customers;
    if (customerIDs == null || customerIDs.isEmpty()) {
      customers = customerRepository.findAll();
    } else {
      customers = customerRepository.findAllById(customerIDs);
    }

    List<Long> activeCustomerIDs = new ArrayList<>();
    long totalHours = 0;

    for (Customer customer : customers) {
      if (customer.isActive()) {
        activeCustomerIDs.add(customer.getCustomerID());
      }
    }

    if (!activeCustomerIDs.isEmpty()) {
      List<PredefinedMeeting> meetings = predefinedMeetingRepository.findAllByCustomerIDsAndDateRangeAndIsActive(activeCustomerIDs, startDate, endDate);
      totalHours = meetings.stream()
              .mapToLong(meeting -> Duration.between(meeting.getStartTime().toLocalTime(), meeting.getEndTime().toLocalTime()).toHours())
              .sum();
    }

    MeetingPlanResponseDTO response = new MeetingPlanResponseDTO(activeCustomerIDs, startDate, endDate, totalHours);
    log.info("Successfully fetched planned hours: {}", response);
    return response;
  }

  public UpdateValidUntilResponseDTO updateMeeting(Long ID, UpdateValidUntilResponseDTO updateRequest) {
    log.info("Updating meeting with ID: {} with request: {}", ID, updateRequest);
    PredefinedMeeting meeting = predefinedMeetingRepository.findById(ID)
            .orElseThrow(() -> new ResourceNotFoundException("Meeting not found"));

    Customer customer = meeting.getCustomer();
    if (!customer.getCustomerID().equals(updateRequest.getCustomerID())) {
      throw new ResourceNotFoundException("Meeting does not belong to the customer");
    }

    meeting.setValidUntil(updateRequest.getValidUntil());
    PredefinedMeeting updatedMeeting = predefinedMeetingRepository.save(meeting);

    UpdateValidUntilResponseDTO responseDTO = new UpdateValidUntilResponseDTO();
    responseDTO.setCustomerID(updatedMeeting.getCustomer().getCustomerID());
    responseDTO.setValidUntil(updatedMeeting.getValidUntil());

    log.info("Successfully updated meeting with ID: {}", ID);
    return responseDTO;
  }

  public Customer updateCustomerActiveStatus(Long customerID, UpdateCustomerActiveStatusRequestDTO request) {
    log.info("Updating active status for customer ID: {} with request: {}", customerID, request);
    Customer customer = customerRepository.findById(customerID)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

    if (request.getIsActive()) {
      if (customerRepository.existsBySheetCustomerID(request.getSheetCustomerID())) {
        customer.setActive(true);
      } else {
        customer.setActive(false);
      }
    } else {
      customer.setActive(false);
    }

    Customer updatedCustomer = customerRepository.save(customer);
    log.info("Successfully updated active status for customer ID: {}. New status: {}", customerID, updatedCustomer.isActive() ? "active" : "inactive");
    return updatedCustomer;
  }

  public PredefinedMeetingDTO toDTO(PredefinedMeeting meeting) {
    PredefinedMeetingDTO dto = new PredefinedMeetingDTO();
    dto.setId(meeting.getID());

    if (meeting.getValidFrom() != null) {
      dto.setValidFrom(Date.valueOf(meeting.getValidFrom().toLocalDate()));
    }

    if (meeting.getValidUntil() != null) {
      dto.setValidUntil(Date.valueOf(meeting.getValidUntil().toLocalDate()));
    } else {
      dto.setValidUntil(null);
    }

    if (meeting.getStartTime() != null) {
      dto.setStartTime(Time.valueOf(meeting.getStartTime().toLocalTime()));
    }

    if (meeting.getEndTime() != null) {
      dto.setEndTime(Time.valueOf(meeting.getEndTime().toLocalTime()));
    }

    Customer customer = meeting.getCustomer();
    if (customer != null) {
      dto.setCustomerID(customer.getCustomerID());
      dto.setSheetCustomerID(customer.getSheetCustomerID());
      dto.setIsActive(customer.isActive());
    }

    Week week = meeting.getWeek();
    if (week != null) {
      dto.setWeekID(week.getId());
      dto.setWeekDay(week.getWeekDay());
    }

    return dto;
  }
}
