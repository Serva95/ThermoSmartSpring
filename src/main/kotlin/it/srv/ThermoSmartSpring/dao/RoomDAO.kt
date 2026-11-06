package it.srv.ThermoSmartSpring.dao

import it.srv.ThermoSmartSpring.model.Room
import it.srv.ThermoSmartSpring.repo.RoomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
open class RoomDAO {
    @Autowired
    lateinit var repo: RoomRepository

    open fun getAll(asc: Boolean): Iterable<Room> = if (asc) repo.findAllByOrderByNomeAsc() else repo.findAllByOrderByNomeDesc()

    open fun save(room: Room): Room = repo.save(room)

    open fun get(id: Long): Room = repo.getById(id)

    open fun getBySensorId(id: String): Room = repo.findBySensor_Id(id)

    open fun delete(id: Long) { repo.deleteById(id) }

    open fun exists(room: Room?): Boolean = room != null
}
