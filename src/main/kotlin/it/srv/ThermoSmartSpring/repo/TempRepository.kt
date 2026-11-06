package it.srv.ThermoSmartSpring.repo

import it.srv.ThermoSmartSpring.dto.AVGDTO
import it.srv.ThermoSmartSpring.model.Temp
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface TempRepository : CrudRepository<Temp, Long> {
    fun findFirstBySensorIdOrderByCreatedAtDesc(sensorId: String): Temp?

    fun findTop150BySensorIdOrderByCreatedAtDesc(sensorid: String): List<Temp>

    @Query(value = "SELECT CAST(createdat AS DATE) AS giorno, AVG(temp) AS temp FROM temps WHERE sensorid = ?1 GROUP BY giorno ORDER BY giorno DESC LIMIT ?2", nativeQuery = true)
    fun findAVGVals(id: String, limit: Int): List<AVGDTO>

    fun findBySensorIdOrderByCreatedAtDesc(sensorID: String, pageable: Pageable): List<Temp>
}
