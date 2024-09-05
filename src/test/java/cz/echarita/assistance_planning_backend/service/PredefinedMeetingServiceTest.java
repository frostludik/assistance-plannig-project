package cz.echarita.assistance_planning_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cz.echarita.assistance_planning_backend.controller.dto.UpdateValidUntilResponseDTO;
import cz.echarita.assistance_planning_backend.exception.ResourceNotFoundException;
import cz.echarita.assistance_planning_backend.model.Customer;
import cz.echarita.assistance_planning_backend.model.PredefinedMeeting;
import cz.echarita.assistance_planning_backend.repository.CustomerRepository;
import cz.echarita.assistance_planning_backend.repository.PredefinedMeetingRepository;
import java.sql.Date;
import java.sql.Time;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PredefinedMeetingServiceTest {

  @Mock
  private PredefinedMeetingRepository predefinedMeetingRepository;

  @Mock
  private CustomerRepository customerRepository;

  @InjectMocks
  private PredefinedMeetingService predefinedMeetingService;

  private Customer customer;
  private PredefinedMeeting meeting;

  @BeforeEach
  void setUp() {
    customer = new Customer();
    customer.setCustomerID(1L);
    customer.setSheetCustomerID("client1");

    meeting = new PredefinedMeeting();
    meeting.setID(9L);
    meeting.setCustomer(customer);
    meeting.setValidFrom(Date.valueOf("2024-07-19"));
    meeting.setValidUntil(Date.valueOf("2024-07-20"));
    meeting.setStartTime(Time.valueOf("10:00:00"));
    meeting.setEndTime(Time.valueOf("11:00:00"));
  }

  @Test
  void testUpdateMeeting() {
    UpdateValidUntilResponseDTO updateRequest = new UpdateValidUntilResponseDTO();
    updateRequest.setCustomerID(9L);
    updateRequest.setValidUntil(Date.valueOf("2025-07-23"));

    when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
    when(predefinedMeetingRepository.findById(9L)).thenReturn(Optional.of(meeting));
    when(predefinedMeetingRepository.save(any(PredefinedMeeting.class))).thenAnswer(
        invocation -> invocation.getArgument(0));

    UpdateValidUntilResponseDTO result = predefinedMeetingService.updateMeeting(1L, updateRequest);

    assertNotNull(result);
    assertEquals(9L, result.getCustomerID());
    assertEquals(1L, result.getCustomerID());
    assertEquals(Date.valueOf("2025-07-23"), result.getValidUntil());

    verify(customerRepository, times(1)).findById(1L);
    verify(predefinedMeetingRepository, times(1)).findById(9L);
    verify(predefinedMeetingRepository, times(1)).save(any(PredefinedMeeting.class));
  }

  @Test
  void testUpdateMeeting_CustomerNotFound() {
    UpdateValidUntilResponseDTO updateRequest = new UpdateValidUntilResponseDTO();
    updateRequest.setCustomerID(9L);
    updateRequest.setValidUntil(Date.valueOf("2025-07-23"));

    when(customerRepository.findById(1L)).thenReturn(Optional.empty());

    Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
      predefinedMeetingService.updateMeeting(1L, updateRequest);
    });

    assertEquals("Customer not found", exception.getMessage());

    verify(customerRepository, times(1)).findById(1L);
    verify(predefinedMeetingRepository, times(0)).findById(anyLong());
    verify(predefinedMeetingRepository, times(0)).save(any(PredefinedMeeting.class));
  }

  @Test
  void testUpdateMeeting_MeetingNotFound() {
    UpdateValidUntilResponseDTO updateRequest = new UpdateValidUntilResponseDTO();
    updateRequest.setCustomerID(9L);
    updateRequest.setValidUntil(Date.valueOf("2025-07-23"));

    when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
    when(predefinedMeetingRepository.findById(9L)).thenReturn(Optional.empty());

    Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
      predefinedMeetingService.updateMeeting(1L, updateRequest);
    });

    assertEquals("Meeting not found", exception.getMessage());

    verify(customerRepository, times(1)).findById(1L);
    verify(predefinedMeetingRepository, times(1)).findById(9L);
    verify(predefinedMeetingRepository, times(0)).save(any(PredefinedMeeting.class));
  }

  @Test
  void testUpdateMeeting_MeetingDoesNotBelongToCustomer() {
    UpdateValidUntilResponseDTO updateRequest = new UpdateValidUntilResponseDTO();
    updateRequest.setCustomerID(9L);
    updateRequest.setValidUntil(Date.valueOf("2025-07-23"));

    Customer anotherCustomer = new Customer();
    anotherCustomer.setCustomerID(2L);
    meeting.setCustomer(anotherCustomer);

    when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
    when(predefinedMeetingRepository.findById(9L)).thenReturn(Optional.of(meeting));

    Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
      predefinedMeetingService.updateMeeting(1L, updateRequest);
    });

    assertEquals("Meeting does not belong to the customer", exception.getMessage());

    verify(customerRepository, times(1)).findById(1L);
    verify(predefinedMeetingRepository, times(1)).findById(9L);
    verify(predefinedMeetingRepository, times(0)).save(any(PredefinedMeeting.class));
  }

}
