package it.srv.ThermoSmartSpring.repo

import it.srv.ThermoSmartSpring.model.OrariOnOff
import org.springframework.data.repository.CrudRepository
import java.util.List

interface OrariOnOffRepository : CrudRepository<OrariOnOff, Long> {
    fun findByRoom_IdOrderByGiorno(roomid: Long): List<OrariOnOff>

    fun getById(id: Long): OrariOnOff
}
