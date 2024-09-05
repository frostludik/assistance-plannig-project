package cz.echarita.assistance_planning_backend.service;

import cz.echarita.assistance_planning_backend.model.Meeting;
import cz.echarita.assistance_planning_backend.repository.MeetingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class MeetingServiceTest {

    @Mock
    private MeetingRepository meetingRepository;

    @InjectMocks
    private MeetingService meetingService;
    
    @Test
    public void testSaveMeeting() {
        Meeting meeting = new Meeting();
        meeting.setAssistID("1");
        meeting.setClientID("2");
        meeting.setStartDateTime(OffsetDateTime.now().toLocalDateTime());
        meeting.setEndDateTime(OffsetDateTime.now().plusHours(1).toLocalDateTime());
        meeting.setComment("Test comment");

        when(meetingRepository.save(meeting)).thenReturn(meeting);

        Meeting savedMeeting = meetingService.createMeeting(meeting);

        assertEquals(meeting, savedMeeting);
    }

    @Test
    public void testGetMeetingById() {
        Long meetingId = 1L;
        Meeting expectedMeeting = new Meeting();
        expectedMeeting.setId(meetingId);
        expectedMeeting.setAssistID("1");
        expectedMeeting.setClientID("2");
        expectedMeeting.setStartDateTime(OffsetDateTime.now().toLocalDateTime());
        expectedMeeting.setEndDateTime(OffsetDateTime.now().plusHours(1).toLocalDateTime());
        expectedMeeting.setComment("Test comment");

        when(meetingRepository.findById(meetingId)).thenReturn(java.util.Optional.of(expectedMeeting));

        Meeting actualMeeting = meetingService.getMeetingById(meetingId);

        assertEquals(expectedMeeting, actualMeeting);
    }

}
