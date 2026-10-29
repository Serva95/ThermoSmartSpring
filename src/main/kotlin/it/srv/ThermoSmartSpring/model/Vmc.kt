package it.srv.ThermoSmartSpring.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalTime

@Table(name = "vmcs")
@Entity
class Vmc(
    @Id
    var id: String,

    @Column(name = "statoattuale")
    var statoAttuale: Boolean? = null,

    @Column(name = "impostazionefunzione")
    var impostazioneFunzione: Boolean? = null,

    @Column(name = "programmedontime")
    var programmedOnTime: LocalTime? = null,

    @Column(name = "programmedofftime")
    var programmedOffTime: LocalTime? = null
)
