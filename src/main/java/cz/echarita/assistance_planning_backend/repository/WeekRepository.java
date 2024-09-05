package cz.echarita.assistance_planning_backend.repository;

import cz.echarita.assistance_planning_backend.model.Week;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeekRepository extends JpaRepository<Week, Long> {
}
