package cz.echarita.assistance_planning_backend.utilities;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateUtilsTest {
    @Test
    public void testGetStartDate() {
        LocalDate expectedStartDate = LocalDate.of(2024, 7, 15);
        LocalDate actualStartDate = DateUtils.getStartDate(29, 2024);
        assertEquals(expectedStartDate, actualStartDate, "The start date should 15.7.2024");
    }

    @Test
    public void testGetEndDate() {
        LocalDate expectedEndDate = LocalDate.of(2024, 7, 21);
        LocalDate actualEndDate = DateUtils.getEndDate(29, 2024);
        assertEquals(expectedEndDate, actualEndDate, "The end date should 21.7.2024");
    }
}
