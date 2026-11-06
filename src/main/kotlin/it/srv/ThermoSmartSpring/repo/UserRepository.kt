package it.srv.ThermoSmartSpring.repo

import it.srv.ThermoSmartSpring.model.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long> {
    fun findByEmail(email: String): User?

    fun findByUsername(username: String): User?
}
