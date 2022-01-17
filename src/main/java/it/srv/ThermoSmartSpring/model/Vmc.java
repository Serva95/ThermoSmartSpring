package it.srv.ThermoSmartSpring.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalTime;

@Table(name = "vmcs")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Vmc {
    @Id
    @NonNull
    private String id;
    @Column(name = "statoattuale")
    private Boolean statoAttuale;
    @Column(name = "impostazionefunzione")
    private Boolean impostazioneFunzione;
    @Column(name = "programmedontime")
    private LocalTime programmedOnTime;
    @Column(name = "programmedofftime")
    private LocalTime programmedOffTime;
}
