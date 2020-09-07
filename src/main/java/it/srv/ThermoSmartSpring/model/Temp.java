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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private BigDecimal temp;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "sensor_id")
    private String sensorId;
}
