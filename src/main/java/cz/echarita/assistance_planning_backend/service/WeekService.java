package cz.echarita.assistance_planning_backend.service;

import cz.echarita.assistance_planning_backend.model.Week;
import cz.echarita.assistance_planning_backend.repository.WeekRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WeekService {

    @Autowired
    private WeekRepository weekRepository;

    public Week addWeek(String weekDay) {
        log.info("Adding week with weekDay: {}", weekDay);
        Week week = new Week();
        week.setWeekDay(weekDay);
        Week savedWeek = weekRepository.save(week);
        log.info("Successfully added week with ID: {}", savedWeek.getId());
        return savedWeek;
    }
}
