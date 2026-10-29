package it.srv.ThermoSmartSpring.model

import jakarta.persistence.*

@Table(name = "authorities")
@Entity
class Authorities(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var username: String? = null,
    var authority: String? = null
) {
    constructor(username: String, authority: String) : this(0, username, authority)
}
