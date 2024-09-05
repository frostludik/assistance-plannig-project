package cz.echarita.assistance_planning_backend.repository;

import cz.echarita.assistance_planning_backend.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    List<Meeting> findByAssistID(String assistID);

    @Query("SELECT m FROM Meeting m WHERE m.assistID = :assistID AND m.startDateTime  >= :since AND m.endDateTime  <= :until")
    List<Meeting> findByAssistIDAndStartDateTimeBetween(
            @Param("assistID") String assistID,
            @Param("since") LocalDateTime since,
            @Param("until") LocalDateTime until);


    List<Meeting> findByClientID(String clientID);

    @Query("SELECT m FROM Meeting m WHERE m.clientID = :clientID AND m.startDateTime  >= :since AND m.endDateTime  <= :until")
    List<Meeting> findByClientIDAndStartDateTimeBetween(
            @Param("clientID") String clientID,
            @Param("since") LocalDateTime since,
            @Param("until") LocalDateTime until);


    @Query("SELECT m FROM Meeting m WHERE m.id <> :id")
    List<Meeting> findAllExceptId(@Param("id") Long id);

}
