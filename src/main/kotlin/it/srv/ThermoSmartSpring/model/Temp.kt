package it.srv.ThermoSmartSpring.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Table(name = "temps")
@Entity
class Temp(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var temp: BigDecimal? = null,

    @Column(name = "createdat")
    var createdAt: LocalDateTime? = null,

    @ManyToOne(targetEntity = Sensor::class, fetch = FetchType.LAZY)
    @JoinColumn(name = "sensorid", referencedColumnName = "id")
    var sensor: Sensor? = null
) {
    val createdDateTimeFormatted: String get() = createdAt?.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) ?: ""
    val createdTimeFormatted: String get() = createdAt?.format(DateTimeFormatter.ofPattern("HH:mm:ss")) ?: ""
}
