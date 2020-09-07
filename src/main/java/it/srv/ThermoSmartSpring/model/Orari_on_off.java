package it.srv.ThermoSmartSpring.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;

@Table(name = "orari_on_offs")
@Entity
@Getter @Setter
public class Orari_on_off {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @Column(name = "room_id")
    private long roomId;
    private short giorno;
    private short fascia;
    @Column(name = "orario_accensione")
    private LocalTime orarioAccensione;
    @Column(name = "orario_spegnimento")
    private LocalTime orarioSpegnimento;

}
