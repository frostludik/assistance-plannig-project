package cz.echarita.assistance_planning_backend.controller.dto;

import cz.echarita.assistance_planning_backend.model.PredefinedMeeting;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomerResponse {

  Long clientId;

  String sheetCustomerId;

  PredefinedMeeting definedMeeting;

  boolean isActive;
}
