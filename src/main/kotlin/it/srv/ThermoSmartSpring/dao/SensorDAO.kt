package it.srv.ThermoSmartSpring.dao

import it.srv.ThermoSmartSpring.model.Sensor
import it.srv.ThermoSmartSpring.repo.SensorRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
open class SensorDAO {
    @Autowired
    lateinit var repo: SensorRepository

    open fun getAll(asc: Boolean): Iterable<Sensor> = if (asc) repo.findAllByOrderByIdAsc() else repo.findAllByOrderByIdDesc()

    open fun save(sensor: Sensor): Sensor = repo.save(sensor)

    open fun get(id: String): Sensor = repo.findById(id).orElse(null) as Sensor

    open fun delete(id: String) { repo.deleteById(id) }

    open fun exists(sensor: Sensor?): Boolean = sensor != null

    open fun exists(id: String): Boolean {
        val s = get(id)
        return s != null
    }

    open fun update(sensor: Sensor, oldID: String) { repo.updateSensorById(sensor.id, sensor.nome, sensor.location, oldID) }

    open fun findAllRoomIsNull(): Iterable<Sensor> = repo.findAllByRoom_SensorIsNull()

    open fun findNotAssociatedPlusActual(id: String): Iterable<Sensor> = repo.findAllByRoom_SensorIsNullOrIdEquals(id)
}
