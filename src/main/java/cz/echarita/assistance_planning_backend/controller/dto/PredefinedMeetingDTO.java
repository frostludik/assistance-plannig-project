package cz.echarita.assistance_planning_backend.controller.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({ "id", "isActive", "validFrom", "validUntil", "startTime", "endTime", "customerID", "sheetCustomerID", "weekID", "weekDay" })
public class PredefinedMeetingDTO {
    private Long id;
    private java.sql.Date validFrom;
    private java.sql.Date  validUntil;
    private java.sql.Time startTime;
    private java.sql.Time endTime;
    private Long weekID;
    private String weekDay;
    private Long customerID;
    private String sheetCustomerID;
    private Boolean isActive;
}

