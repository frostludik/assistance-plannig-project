package cz.echarita.assistance_planning_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "predefined_meeting")
public class PredefinedMeeting {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long ID;

    @ManyToOne
    @JoinColumn(name = "user_ID", referencedColumnName = "customer_ID")
    private Customer customer;

    @Column(name = "valid_from", nullable = false)
    private java.sql.Date validFrom;

    @Column(name = "valid_until")
    private java.sql.Date validUntil;

    @Column(name = "start_time", nullable = false)
    private java.sql.Time startTime;

    @Column(name = "end_time", nullable = false)
    private java.sql.Time endTime;

    @ManyToOne
    @JoinColumn(name = "week_ID", referencedColumnName = "ID")
    private Week week;
}
