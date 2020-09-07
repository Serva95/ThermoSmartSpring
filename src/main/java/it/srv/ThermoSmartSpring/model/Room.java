package it.srv.ThermoSmartSpring.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "rooms")
@Entity
@Getter @Setter
@ToString
public class Room {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String nome;
    @Column(name = "max_temp")
    private BigDecimal maxTemp;
    @Column(name = "min_temp")
    private BigDecimal minTemp;
    @Column(name = "absolute_min")
    private BigDecimal absoluteMin;
    @Column(name = "sensor_id")
    private String sensorId;
    @Column(name = "manual_active")
    private boolean manualActive;
    @Column(name = "manual_inactive")
    private boolean manualInactive;
    @Column(name = "manual_off")
    private boolean manualOff;
}
