package cz.echarita.assistance_planning_backend.utilities;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class DateUtils {
    public static LocalDate getStartDate(int week, int year) {
        LocalDate firstDayOfYear = LocalDate.of(year, 1, 1);
        LocalDate firstMonday = firstDayOfYear.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));

        return firstMonday.plusWeeks(week - 1);
    }

    public static LocalDate getEndDate(int week, int year) {
        LocalDate startDate = getStartDate(week, year);

        return startDate.plusDays(6);
    }
}
