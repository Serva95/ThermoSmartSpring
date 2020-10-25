package it.srv.ThermoSmartSpring.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    @ManyToOne(targetEntity = Sensor.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "sensorid", referencedColumnName = "id")
    private Sensor sensor;

    public String getCreatedDateTimeFormatted(){ return this.createdAt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")); }
    public String getCreatedTimeFormatted(){ return this.createdAt.format(DateTimeFormatter.ofPattern("HH:mm:ss")); }
}
