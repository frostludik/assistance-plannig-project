package cz.echarita.assistance_planning_backend.repository;

import cz.echarita.assistance_planning_backend.model.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

  Optional<Customer> findBySheetCustomerID(String sheetCustomerID);

  boolean existsBySheetCustomerID(String sheetCustomerID);

}
