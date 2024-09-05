package cz.echarita.assistance_planning_backend.service;

import cz.echarita.assistance_planning_backend.controller.dto.UpdateCustomerActiveStatusRequestDTO;
import cz.echarita.assistance_planning_backend.exception.ResourceNotFoundException;
import cz.echarita.assistance_planning_backend.model.Customer;
import cz.echarita.assistance_planning_backend.repository.CustomerRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerService {

  @Autowired
  private CustomerRepository customerRepository;

  public Customer addCustomer(String sheetCustomerID) {
    log.info("Adding customer with sheetCustomerID: {}", sheetCustomerID);
    Customer customer = new Customer();
    customer.setSheetCustomerID(sheetCustomerID);
    Customer savedCustomer = customerRepository.save(customer);
    log.info("Successfully added customer with ID: {}", savedCustomer.getCustomerID());
    return savedCustomer;
  }

  public Customer updateCustomerActiveStatus(Long customerID,
                                             UpdateCustomerActiveStatusRequestDTO request) {
    log.info("Updating active status for customer ID: {} with request: {}", customerID, request);
    Customer customer;

    Optional<Customer> existingCustomer = customerRepository.findById(customerID);

    if (existingCustomer.isPresent()) {
      customer = existingCustomer.get();
      log.info("Found existing customer with ID: {}", customerID);
    } else {
      if (request.getIsActive() != null && request.getIsActive()) {
        log.info("Customer not found, creating new customer with ID: {}", customerID);
        customer = new Customer();
        customer.setCustomerID(customerID);
        customer.setSheetCustomerID(request.getSheetCustomerID());
        customer.setActive(true);
        Customer savedCustomer = customerRepository.save(customer);
        log.info("Successfully created and activated new customer with ID: {}", savedCustomer.getCustomerID());
        return savedCustomer;
      } else {
        log.error("Customer not found and cannot be created with isActive set to false.");
        throw new ResourceNotFoundException(
                "Customer not found and cannot be created with isActive set to false.");
      }
    }

    if (request.getIsActive() != null) {
      if (request.getIsActive()) {
        if (customerRepository.existsBySheetCustomerID(request.getSheetCustomerID())) {
          log.info("Setting customer ID: {} to active", customerID);
          customer.setActive(true);
        } else {
          log.info("SheetCustomerID not found, setting customer ID: {} to inactive", customerID);
          customer.setActive(false);
        }
      } else {
        log.info("Setting customer ID: {} to inactive", customerID);
        customer.setActive(false);
      }
    } else {
      log.info("No active status provided, setting customer ID: {} to inactive", customerID);
      customer.setActive(false);
    }

    Customer updatedCustomer = customerRepository.save(customer);
    log.info("Successfully updated customer with ID: {}", updatedCustomer.getCustomerID());
    return updatedCustomer;
  }
}
