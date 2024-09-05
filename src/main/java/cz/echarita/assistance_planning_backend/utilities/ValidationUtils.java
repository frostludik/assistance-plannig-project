package cz.echarita.assistance_planning_backend.utilities;

import cz.echarita.assistance_planning_backend.model.Meeting;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

public class ValidationUtils {


    public static boolean isValidMeetingTime(Meeting meeting) {
        LocalDateTime startDateTime = meeting.getStartDateTime();
        LocalDateTime endDateTime = meeting.getEndDateTime();
        return startDateTime != null && endDateTime != null && startDateTime.isBefore(endDateTime);
    }

    public static boolean isOverlapping(Meeting newMeeting, List<Meeting> existingMeetings) {
        for (Meeting existingMeeting : existingMeetings) {
            if (Objects.equals(existingMeeting.getAssistID(), newMeeting.getAssistID()) &&
                    newMeeting.getStartDateTime().isBefore(existingMeeting.getEndDateTime()) &&
                    newMeeting.getEndDateTime().isAfter(existingMeeting.getStartDateTime())) {
                return true;
            }
        }
        return false;
    }
}
