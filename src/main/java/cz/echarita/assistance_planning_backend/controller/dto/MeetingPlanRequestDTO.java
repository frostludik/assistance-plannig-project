package cz.echarita.assistance_planning_backend.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MeetingPlanRequestDTO {
    private List<Long> customerIDs;
    private java.sql.Date date;
}
