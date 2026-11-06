package it.srv.ThermoSmartSpring.repo

import it.srv.ThermoSmartSpring.model.Info
import org.springframework.data.repository.CrudRepository

interface InfoRepository : CrudRepository<Info, String> {
    fun findAllByOrderByIdAsc(): Iterable<Info>
}
