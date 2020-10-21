package it.srv.ThermoSmartSpring.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Table(name = "rooms")
@Entity
@Getter @Setter
@ToString
public class Room {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    private String nome;
    @Column(name = "maxtemp")
    private BigDecimal maxTemp;
    @Column(name = "mintemp")
    private BigDecimal minTemp;
    @Column(name = "absolutemin")
    private BigDecimal absoluteMin;
    @Column(name = "manualactive")
    private boolean manualActive;
    @Column(name = "manualinactive")
    private boolean manualInactive;
    @Column(name = "manualoff")
    private boolean manualOff;

    @OneToOne(targetEntity = Sensor.class, fetch= FetchType.LAZY)
    @JoinColumn(name = "sensorid", referencedColumnName = "id")
    private Sensor sensor;

    @OneToMany(mappedBy = "room")
    private List<OrariOnOff> orariOnOff;
}
