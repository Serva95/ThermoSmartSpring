package it.srv.ThermoSmartSpring.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "info")
class Info(
    @Id
    @Column(name = "id", nullable = false)
    var id: String,

    @Column(name = "value", nullable = false)
    var value: String,

    @Column(name = "time")
    var time: LocalDateTime? = null
)
