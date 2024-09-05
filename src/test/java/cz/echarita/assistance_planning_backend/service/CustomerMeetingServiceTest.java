package cz.echarita.assistance_planning_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cz.echarita.assistance_planning_backend.controller.dto.CustomerResponse;
import cz.echarita.assistance_planning_backend.model.PredefinedMeeting;
import cz.echarita.assistance_planning_backend.repository.PredefinedMeetingRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("h2")
@SpringBootTest
@Log4j2
class CustomerMeetingServiceTest {


  @Autowired
  private PredefinedMeetingRepository predefinedMeetingRepository;

  @Autowired
  private CustomerMeetingService customerMeetingService;


  private final int[] ALL_USERS_SIZE_OF_CUSTOMER_ID = {6, 3, 3};
  private final long[] ALL_USER_USERID = {1, 2, 3};
  private final long[] ALL_USER_ACTIVE_MEETINGS = {4, 2, 2};
  private final long[] ALL_USER_INACTIVE_MEETINGS = {2, 1, 1};

  @RepeatedTest(3)
  public void read_meetings_for_customer_2(RepetitionInfo repetitionInfo) {
    log.info("Repetition #" + repetitionInfo.getCurrentRepetition());
    int positionInField = repetitionInfo.getCurrentRepetition() - 1;
    List<Long> userId = Arrays.asList(ALL_USER_USERID[positionInField]);

    List<PredefinedMeeting> byCustomerCustomerIDInOrderByCustomerCustomerIDDesc =
        predefinedMeetingRepository.findByCustomerCustomerIDInOrderByCustomerCustomerIDDesc(userId);

    System.out.println(byCustomerCustomerIDInOrderByCustomerCustomerIDDesc);
    assertEquals(ALL_USERS_SIZE_OF_CUSTOMER_ID[positionInField],
        byCustomerCustomerIDInOrderByCustomerCustomerIDDesc.size());

    LocalDate localDate = LocalDate.of(2024, 7, 23);
    List<CustomerResponse> customerResponses = customerMeetingService.sortingAndSplitMeetingTwoList(
        byCustomerCustomerIDInOrderByCustomerCustomerIDDesc, localDate);

    assertEquals(1, customerResponses.size());
   

  }

}