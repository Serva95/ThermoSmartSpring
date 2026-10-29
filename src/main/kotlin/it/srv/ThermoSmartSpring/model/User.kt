package it.srv.ThermoSmartSpring.model

import jakarta.persistence.*

@Table(name = "users")
@Entity
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false)
    var email: String = "",

    @Column(nullable = false)
    var password: String = "",

    @Column(nullable = false)
    var username: String = ""
)
