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
public class UpdateMeetingRequestDTO {
    private Long customerID;
    private java.sql.Date validUntil;
}
