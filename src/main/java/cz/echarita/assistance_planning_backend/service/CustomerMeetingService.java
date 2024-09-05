package cz.echarita.assistance_planning_backend.service;

import cz.echarita.assistance_planning_backend.controller.dto.CustomerResponse;
import cz.echarita.assistance_planning_backend.model.PredefinedMeeting;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomerMeetingService {

  public List<CustomerResponse> sortingAndSplitMeetingTwoList(
          List<PredefinedMeeting> predefinedMeetings, LocalDate date) {

    log.info("Sorting and splitting {} predefined meetings for date: {}", predefinedMeetings.size(), date);

    List<CustomerResponse> customerResponses = new ArrayList<>();

    for (PredefinedMeeting predefinedMeeting : predefinedMeetings) {
      CustomerResponse oneClientResponse = new CustomerResponse();
      oneClientResponse.setSheetCustomerId(predefinedMeeting.getCustomer().getSheetCustomerID());
      oneClientResponse.setClientId(predefinedMeeting.getCustomer().getCustomerID());
      oneClientResponse.setDefinedMeeting(predefinedMeeting);
      boolean isActive = this.isMeetingActive(predefinedMeeting, date);
      oneClientResponse.setActive(isActive);
      log.info("Processed meeting for customer ID: {}. Active status: {}", predefinedMeeting.getCustomer().getCustomerID(), isActive ? "active" : "inactive");
      customerResponses.add(oneClientResponse);
    }

    log.info("Total customer responses generated: {}", customerResponses.size());
    return customerResponses;
  }

  private boolean isMeetingActive(PredefinedMeeting meeting, LocalDate dateToShow) {
    ZoneId defaultZoneId = ZoneId.systemDefault();

    Date today = Date.from(dateToShow.atStartOfDay(defaultZoneId).toInstant());

    return (meeting.getValidUntil() == null ||
            (today.after(meeting.getValidFrom()) && today.before(meeting.getValidUntil())));
  }

  @Getter
  @Setter
  @AllArgsConstructor
  class SeparateMeeting {

    List<PredefinedMeeting> definedMeetingsActive;
    List<PredefinedMeeting> definedMeetingsInactive;
  }

}
