package it.srv.ThermoSmartSpring.repo

import it.srv.ThermoSmartSpring.model.Sensor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface SensorRepository : CrudRepository<Sensor, String> {
    fun findAllByOrderByIdAsc(): Iterable<Sensor>

    fun findAllByOrderByIdDesc(): Iterable<Sensor>

    @Modifying
    @Query("update Sensor s set s.id = ?1, s.nome = ?2, s.location = ?3 where s.id = ?4")
    fun updateSensorById(id: String, nome: String, location: String, oldID: String)

    fun findAllByRoom_SensorIsNull(): Iterable<Sensor>

    fun findAllByRoom_SensorIsNullOrIdEquals(id: String): Iterable<Sensor>
}
