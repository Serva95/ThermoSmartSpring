package it.srv.ThermoSmartSpring

import it.srv.ThermoSmartSpring.dao.RoomDAO
import it.srv.ThermoSmartSpring.dao.SensorDAO
import it.srv.ThermoSmartSpring.dao.TempDAO
import it.srv.ThermoSmartSpring.model.Room
import it.srv.ThermoSmartSpring.model.Temp
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView
import java.time.LocalDateTime

@RestController
@RequestMapping("/temps")
class TempController(
    private val tempDAO: TempDAO,
    private val roomDAO: RoomDAO,
    private val sensorDAO: SensorDAO
) {

    @GetMapping("")
    fun Temps(mav: ModelAndView): ModelAndView {
        mav.setViewName("temps")
        val lastTemps = mutableMapOf<String, String>()
        val sensorReadError = mutableMapOf<String, Boolean>()
        val rooms = roomDAO.getAll(true)
        
        for (t in rooms) {
            t.sensor?.let { sensor ->
                val tm = tempDAO.findLastBySensor(sensor.id)
                if (tm != null) {
                    sensorReadError[sensor.id] = tm.createdAt?.isBefore(LocalDateTime.now().minusMinutes(31)) ?: false
                    lastTemps[sensor.id] = "${tm.temp}°C il: ${tm.createdDateTimeFormatted}"
                }
            }
        }
        
        mav.addObject("sensorReadError", sensorReadError)
        mav.addObject("lastTemps", lastTemps)
        mav.addObject("rooms", rooms)
        return mav
    }

    @GetMapping("/{id}")
    fun viewTemp(mav: ModelAndView, @PathVariable id: String): ModelAndView {
        if (!sensorDAO.exists(id)) {
            mav.addObject("sensor", false)
        } else {
            val last = tempDAO.findLastBySensor(id)
            val room = roomDAO.getBySensorId(id)
            mav.addObject("room", room)
            mav.addObject("last", last)
        }
        mav.setViewName("viewTemps")
        return mav
    }
}
