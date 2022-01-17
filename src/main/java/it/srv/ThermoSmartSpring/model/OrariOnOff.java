package it.srv.ThermoSmartSpring.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;

@Table(name = "orarionoff")
@Entity
@Getter @Setter
public class OrariOnOff {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    @ManyToOne(targetEntity = Room.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "roomid", referencedColumnName = "id")
    private Room room;
    private short giorno;
    @Column(name = "orarioaccensionea")
    private LocalTime orarioAccensioneA;
    @Column(name = "orariospegnimentoa")
    private LocalTime orarioSpegnimentoA;
    @Column(name = "orarioaccensioneb")
    private LocalTime orarioAccensioneB;
    @Column(name = "orariospegnimentob")
    private LocalTime orarioSpegnimentoB;
    @Column(name = "orarioaccensionec")
    private LocalTime orarioAccensioneC;
    @Column(name = "orariospegnimentoc")
    private LocalTime orarioSpegnimentoC;


}
