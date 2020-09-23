package it.srv.ThermoSmartSpring.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;

@Table
@Entity
@Getter @Setter
public class OrariOnOff {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @Column(name = "roomid")
    private long roomId;
    private short giorno;
    private short fascia;
    private LocalTime orarioAccensione;
    private LocalTime orarioSpegnimento;

}
