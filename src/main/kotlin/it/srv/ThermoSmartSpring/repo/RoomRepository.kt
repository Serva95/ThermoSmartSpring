package it.srv.ThermoSmartSpring.repo

import it.srv.ThermoSmartSpring.model.Room
import org.springframework.data.repository.CrudRepository

interface RoomRepository : CrudRepository<Room, Long> {
    fun getById(id: Long): Room

    fun findBySensor_Id(id: String): Room

    fun findAllByOrderByNomeAsc(): Iterable<Room>

    fun findAllByOrderByNomeDesc(): Iterable<Room>
}
