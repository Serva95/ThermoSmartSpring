package it.srv.ThermoSmartSpring.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "temps")
@Entity
@Getter
@Setter
public class Temp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private BigDecimal temp;
    @Column(name = "createdat")
    private LocalDateTime createdAt;
    @Column(name = "sensorid")
    private String sensorId;
}
