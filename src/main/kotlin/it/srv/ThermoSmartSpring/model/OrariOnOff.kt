package it.srv.ThermoSmartSpring.model

import jakarta.persistence.*
import java.time.LocalTime

@Table(name = "orarionoff")
@Entity
class OrariOnOff(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(targetEntity = Room::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "roomid", referencedColumnName = "id")
    var room: Room? = null,

    var giorno: Short = 0,

    @Column(name = "orarioaccensionea")
    var orarioAccensioneA: LocalTime? = null,

    @Column(name = "orariospegnimentoa")
    var orarioSpegnimentoA: LocalTime? = null,

    @Column(name = "orarioaccensioneb")
    var orarioAccensioneB: LocalTime? = null,

    @Column(name = "orariospegnimentob")
    var orarioSpegnimentoB: LocalTime? = null,

    @Column(name = "orarioaccensionec")
    var orarioAccensioneC: LocalTime? = null,

    @Column(name = "orariospegnimentoc")
    var orarioSpegnimentoC: LocalTime? = null
)
