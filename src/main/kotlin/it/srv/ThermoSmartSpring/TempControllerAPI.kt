package it.srv.ThermoSmartSpring

import it.srv.ThermoSmartSpring.dao.SensorDAO
import it.srv.ThermoSmartSpring.dao.TempDAO
import it.srv.ThermoSmartSpring.exception.InvalidFieldException
import it.srv.ThermoSmartSpring.model.Temp
import it.srv.ThermoSmartSpring.services.TempService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

@RestController
@RequestMapping("/api/temps")
class TempControllerAPI(
    private val tempDAO: TempDAO,
    private val sensorDAO: SensorDAO,
    private val tempService: TempService
) {

    @GetMapping(path = ["/{id}"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getTempNMeds(@PathVariable id: String): Map<String, List<String>?> {
        val map = mutableMapOf<String, List<String>?>()
        if (!sensorDAO.exists(id)) {
            map["error"] = null
            return map
        }
        val temps = tempService.getTempsClear(id)
        val avgs = tempDAO.findAVGVals(id, 3)
        
        val tempsVal = temps.map { it.temp.toString() }
        val times = temps.map { it.createdTimeFormatted }
        
        val tempsAVG = avgs.map { it.temp.toString().substring(0, 5) }
        val avgDates = avgs.map {
            LocalDate.parse(it.giorno).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        }
        
        map["tempVals"] = tempsVal
        map["times"] = times
        map["tempsAVG"] = tempsAVG
        map["avgDates"] = avgDates
        return map
    }

    @GetMapping(path = ["/{id}/update"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updateTemp(@PathVariable id: String, @RequestParam(name = "previous") previous: String): Map<String, String> {
        val last = tempDAO.findLastBySensor(id)
        val map = mutableMapOf<String, String>()
        try {
            val prev = LocalTime.parse(previous)
            val lastCreatedAt = last?.createdAt
            if (lastCreatedAt != null && prev.isBefore(lastCreatedAt.toLocalTime().truncatedTo(ChronoUnit.SECONDS))) {
                map["temp"] = last.temp.toString()
                map["created"] = last.createdTimeFormatted
            }
            return map
        } catch (e: DateTimeParseException) {
            return map
        }
    }

    @GetMapping(path = ["/{id}/updateMeds"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun updateMeds(@PathVariable id: String, @RequestParam(name = "days") days: Int): Map<String, List<String>> {
        val avgs = tempDAO.findAVGVals(id, days)
        val tempsAVG = avgs.map { it.temp.toString().substring(0, 5) }
        val avgDates = avgs.map {
            LocalDate.parse(it.giorno).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        }
        return mapOf(
            "tempsAVG" to tempsAVG,
            "avgDates" to avgDates
        )
    }

    @PostMapping(value = ["/{id}"], consumes = ["application/json"], produces = ["application/json"])
    fun createTemp(@PathVariable id: String, @RequestBody data: Map<String, String>): ResponseEntity<String> {
        val apiKey = data["apikey"] ?: ""
        val temp = data["temp"] ?: ""
        return try {
            tempService.save(id, temp, apiKey)
            ResponseEntity("{\"status\": \"ok\"}", HttpStatus.CREATED)
        } catch (e: InvalidFieldException) {
            ResponseEntity("{\"status\": \"${e.message}\"}", HttpStatus.BAD_REQUEST)
        }
    }
}
