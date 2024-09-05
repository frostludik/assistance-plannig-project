package cz.echarita.assistance_planning_backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
@Setter
public class MeetingException extends RuntimeException {

  private final HttpStatus status;

  public MeetingException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

}
