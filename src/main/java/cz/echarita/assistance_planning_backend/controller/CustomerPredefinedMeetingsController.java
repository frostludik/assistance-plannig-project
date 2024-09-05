package cz.echarita.assistance_planning_backend.controller;

import cz.echarita.assistance_planning_backend.controller.dto.CustomerRequest;
import cz.echarita.assistance_planning_backend.controller.dto.CustomerResponse;
import cz.echarita.assistance_planning_backend.model.PredefinedMeeting;
import cz.echarita.assistance_planning_backend.repository.PredefinedMeetingRepository;
import cz.echarita.assistance_planning_backend.service.CustomerMeetingService;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/define/customer/meetings")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Customer Predefined Meetings", description = "APIs for managing customer predefined meetings")
public class CustomerPredefinedMeetingsController {

  private final PredefinedMeetingRepository predefinedMeetingRepository;

  private final CustomerMeetingService customerMeetingService;

  @PostMapping
  @CrossOrigin
  @Operation(summary = "Get meetings for a customer", description = "Fetch predefined meetings for a given customer based on their request")
  public List<CustomerResponse> getTheMeetingsForCustomer(
      @RequestBody CustomerRequest customerRequest
  ) {
    log.info(String.format("Request for customer meetings: %s", customerRequest.toString()));
    List<PredefinedMeeting> byCustomerCustomerIDInOrderByCustomerCustomerIDDesc = predefinedMeetingRepository.findByCustomerSheetCustomerIDInOrderByCustomerCustomerIDDesc(
        Arrays.stream(customerRequest.getClients()).toList());
    log.info(String.format("From the customers %s found %s meetings", customerRequest.getClients(),
        byCustomerCustomerIDInOrderByCustomerCustomerIDDesc.size()));

    return customerMeetingService.sortingAndSplitMeetingTwoList(
        byCustomerCustomerIDInOrderByCustomerCustomerIDDesc, customerRequest.getDateToShow());
  }
}
