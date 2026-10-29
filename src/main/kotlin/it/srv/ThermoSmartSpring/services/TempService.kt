package it.srv.ThermoSmartSpring.services

import it.srv.ThermoSmartSpring.dao.SensorDAO
import it.srv.ThermoSmartSpring.dao.TempDAO
import it.srv.ThermoSmartSpring.exception.InvalidFieldException
import it.srv.ThermoSmartSpring.model.Sensor
import it.srv.ThermoSmartSpring.model.Temp
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.time.LocalDateTime

@Service
@Transactional
open class TempService(
    private val tempDAO: TempDAO,
    private val sensorDAO: SensorDAO
) {

    @Throws(InvalidFieldException::class)
    open fun save(sensorID: String, temp: String, apikey: String) {
        var digest: MessageDigest? = null
        val tosha = temp + sensorID
        try {
            digest = MessageDigest.getInstance("SHA-256")
        } catch (ignored: NoSuchAlgorithmException) {
        }
        val hash = digest?.digest(tosha.toByteArray(StandardCharsets.UTF_8)) ?: ByteArray(0)
        val sha256hex = hash.joinToString("") { "%02x".format(it) }
        if (sha256hex == apikey) {
            if (!sensorDAO.exists(sensorID)) {
                throw InvalidFieldException("Id sensore non valido")
            }
            val s = sensorDAO.get(sensorID)
            val t = Temp()
            t.sensor = s
            t.temp = BigDecimal(temp)
            t.createdAt = LocalDateTime.now()
            tempDAO.save(t)
        } else {
            throw InvalidFieldException("apiKey non valida")
        }
    }

    open fun getTempsClear(sensorId: String): List<Temp> {
        val temps = tempDAO.findLastDayBySensor(750, sensorId)
        if (temps.size < 200) {
            return temps
        }
        val toret = mutableListOf<Temp>()
        val iter = temps.iterator()
        while (iter.hasNext()) {
            val avg = Temp()
            var tmp = BigDecimal.ZERO
            for (i in 0 until 5) {
                if (iter.hasNext()) {
                    val next = iter.next()
                    avg.sensor = next.sensor
                    avg.createdAt = next.createdAt
                    avg.id = next.id
                    tmp = tmp.add(next.temp ?: BigDecimal.ZERO)
                } else {
                    tmp = tmp.divide(BigDecimal(i), 2, RoundingMode.HALF_UP)
                    // break loop
                    break
                }
                if (i == 4) {
                    tmp = tmp.divide(BigDecimal(5), 2, RoundingMode.HALF_UP)
                }
            }
            avg.temp = tmp
            toret.add(avg)
        }
        return toret
    }
}
