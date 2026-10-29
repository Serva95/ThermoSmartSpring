package it.srv.ThermoSmartSpring.model

import jakarta.persistence.*

@Table(name = "sensors")
@Entity
class Sensor(
    @Id
    var id: String = "",

    @Column(nullable = false)
    var nome: String = "",

    @Column(nullable = false)
    var location: String = "",

    @OneToOne(mappedBy = "sensor")
    var room: Room? = null,

    @OneToMany(mappedBy = "sensor")
    var temps: List<Temp>? = null
)
