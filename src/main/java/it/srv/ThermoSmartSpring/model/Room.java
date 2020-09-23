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
    @Column(name = "maxtemp")
    private BigDecimal maxTemp;
    @Column(name = "mintemp")
    private BigDecimal minTemp;
    @Column(name = "absolutemin")
    private BigDecimal absoluteMin;
    @Column(name = "sensorid")
    private String sensorId;
    @Column(name = "manualactive")
    private boolean manualActive;
    @Column(name = "manualinactive")
    private boolean manualInactive;
    @Column(name = "manualoff")
    private boolean manualOff;
}
