package it.srv.ThermoSmartSpring.dao

import it.srv.ThermoSmartSpring.dto.AVGDTO
import it.srv.ThermoSmartSpring.model.Temp
import it.srv.ThermoSmartSpring.repo.TempRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
open class TempDAO {
    @Autowired
    lateinit var repo: TempRepository

    open fun save(t: Temp): Temp = repo.save(t)

    open fun findLastBySensor(sensorID: String): Temp? = repo.findFirstBySensorIdOrderByCreatedAtDesc(sensorID)

    open fun findLastDayBySensor(numberOfReads: Int, sensorID: String): List<Temp> {
        val temps = repo.findBySensorIdOrderByCreatedAtDesc(sensorID, PageRequest.of(0, numberOfReads))
        return temps.reversed()
    }

    open fun findAVGVals(sensorID: String, limit: Int): List<AVGDTO> {
        val temps = repo.findAVGVals(sensorID, limit)
        return temps.reversed()
    }
}
