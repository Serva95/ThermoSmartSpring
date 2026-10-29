package it.srv.ThermoSmartSpring.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
