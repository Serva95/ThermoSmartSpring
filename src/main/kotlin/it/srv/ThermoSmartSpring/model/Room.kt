package it.srv.ThermoSmartSpring.model

import jakarta.persistence.*
import java.math.BigDecimal

@Table(name = "rooms")
@Entity
class Room(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var nome: String? = null,

    @Column(name = "maxtemp")
    var maxTemp: BigDecimal? = null,

    @Column(name = "mintemp")
    var minTemp: BigDecimal? = null,

    @Column(name = "absolutemin")
    var absoluteMin: BigDecimal? = null,

    @Column(name = "manualactive")
    var manualActive: Boolean = false,

    @Column(name = "manualinactive")
    var manualInactive: Boolean = false,

    @Column(name = "manualoff")
    var manualOff: Boolean = false,

    @Column(name = "actualstatus")
    var actualStatus: Boolean = false,

    @OneToOne(targetEntity = Sensor::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "sensorid", referencedColumnName = "id")
    var sensor: Sensor? = null,

    @OneToMany(mappedBy = "room")
    var orariOnOff: List<OrariOnOff>? = null
) {
    override fun toString(): String {
        return "Room(id=$id, nome=$nome, maxTemp=$maxTemp, minTemp=$minTemp, absoluteMin=$absoluteMin, manualActive=$manualActive, manualInactive=$manualInactive, manualOff=$manualOff, actualStatus=$actualStatus)"
    }
}
