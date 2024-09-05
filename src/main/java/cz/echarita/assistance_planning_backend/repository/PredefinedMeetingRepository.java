package cz.echarita.assistance_planning_backend.repository;

import cz.echarita.assistance_planning_backend.model.PredefinedMeeting;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PredefinedMeetingRepository extends JpaRepository<PredefinedMeeting, Long> {

  List<PredefinedMeeting> findByCustomerSheetCustomerIDInOrderByCustomerCustomerIDDesc(
      List<String> userIds);

  List<PredefinedMeeting> findAllMeetingsByCustomer_CustomerIDIn(List<Long> customerIDs);

  List<PredefinedMeeting> findAllByCustomer_CustomerIDAndValidFromBetween(Long customerID, LocalDate startDate, LocalDate endDate);


  @Query("SELECT pm FROM PredefinedMeeting pm " +
          "JOIN pm.customer c "+
          "WHERE c.customerID IN :customerIDs " +
          "AND pm.validFrom <= :endDate " +
          "AND pm.validUntil >= :startDate " +
          "AND c.isActive = true")
  List<PredefinedMeeting> findAllByCustomerIDsAndDateRangeAndIsActive(
          @Param("customerIDs") List<Long> customerIDs,
          @Param("startDate") java.sql.Date startDate,
          @Param("endDate") java.sql.Date endDate);
 
  List<PredefinedMeeting> findByCustomerCustomerIDInOrderByCustomerCustomerIDDesc(
      List<Long> userIds);
}