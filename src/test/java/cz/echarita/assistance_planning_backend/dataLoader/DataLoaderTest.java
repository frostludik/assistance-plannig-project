package cz.echarita.assistance_planning_backend.dataLoader;

import cz.echarita.assistance_planning_backend.model.Meeting;
import cz.echarita.assistance_planning_backend.repository.MeetingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@SpringBootTest
public class DataLoaderTest {

    @Autowired
    private MeetingRepository meetingRepository;

    @Test
    public void loadData() {
        Meeting meeting1 = new Meeting();
        meeting1.setAssistID("1");
        meeting1.setClientID("1");
        meeting1.setStartDateTime(OffsetDateTime.now().minusHours(2).toLocalDateTime());
        meeting1.setEndDateTime(OffsetDateTime.now().minusHours(1).toLocalDateTime());
        meeting1.setComment("First meeting");

        Meeting meeting2 = new Meeting();
        meeting2.setAssistID("2");
        meeting2.setClientID("2");
        meeting2.setStartDateTime(OffsetDateTime.now().minusHours(3).toLocalDateTime());
        meeting2.setEndDateTime(OffsetDateTime.now().minusHours(2).toLocalDateTime());
        meeting2.setComment("Second meeting");

        meetingRepository.save(meeting1);
        meetingRepository.save(meeting2);
    }
}
