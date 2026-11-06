package it.srv.ThermoSmartSpring.repo

import it.srv.ThermoSmartSpring.model.Authorities
import org.springframework.data.repository.CrudRepository
import java.util.ArrayList

interface AuthoritiesRepository : CrudRepository<Authorities, Short> {
    // @Query(value = "SELECT * FROM authorities AS a JOIN users AS u ON a.username=u.username WHERE u.username = ?1", nativeQuery = true)
    fun findByUsername(username: String): ArrayList<Authorities>

    fun findByAuthority(authority: String): ArrayList<Authorities>
}
