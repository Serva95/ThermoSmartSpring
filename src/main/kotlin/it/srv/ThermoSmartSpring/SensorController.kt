package it.srv.ThermoSmartSpring

import it.srv.ThermoSmartSpring.dao.SensorDAO
import it.srv.ThermoSmartSpring.exception.BlankFieldsException
import it.srv.ThermoSmartSpring.exception.ObjectAlreadyExistException
import it.srv.ThermoSmartSpring.model.Sensor
import it.srv.ThermoSmartSpring.services.SensorService
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@RestController
@RequestMapping("/sensors")
class SensorController(
    private val sensorDAO: SensorDAO,
    private val sensorService: SensorService
) {

    @GetMapping("")
    fun sensors(mav: ModelAndView): ModelAndView {
        mav.setViewName("sensors")
        val sensors = sensorDAO.getAll(true)
        mav.addObject("sensors", sensors)
        return mav
    }

    @GetMapping("/new")
    fun newSensor(mav: ModelAndView): ModelAndView {
        val sensor = Sensor()
        mav.addObject("sensor", sensor)
        mav.setViewName("newSensor")
        return mav
    }

    @PostMapping("/new")
    fun createSensor(mav: ModelAndView, @ModelAttribute("sensor") sensor: Sensor): ModelAndView {
        try {
            sensorService.saveNewSensor(sensor)
            mav.addObject("message", "Sensore inserito con successo.")
            mav.setViewName("redirect:/sensors")
            return mav
        } catch (e: Exception) {
            if (e is ObjectAlreadyExistException) {
                sensor.id = ""
            }
            mav.addObject("message", e.message)
            mav.addObject("sensor", sensor)
            mav.setViewName("newSensor")
            return mav
        }
    }

    @GetMapping("/{id}")
    fun editSensor(mav: ModelAndView, @PathVariable id: String): ModelAndView {
        mav.addObject("sensor", sensorDAO.get(id))
        mav.setViewName("editSensor")
        return mav
    }

    @PutMapping("/{id}")
    fun updateSensor(mav: ModelAndView, @PathVariable id: String, @ModelAttribute("sensor") newData: Sensor): ModelAndView {
        val sensor = sensorDAO.get(id)
        if (!sensorDAO.exists(sensor)) {
            mav.addObject("message", "Sensore non trovato, riprova.")
            mav.setViewName("redirect:/sensors")
            return mav
        }
        try {
            sensorService.updateSensor(sensor, newData)
            mav.setViewName("redirect:/sensors")
            return mav
        } catch (e: Exception) {
            mav.addObject("sensor", sensor)
            mav.setViewName("editSensor")
            mav.addObject("message", e.message)
            return mav
        }
    }

    @DeleteMapping("/{id}")
    fun deleteSensor(mav: ModelAndView, @PathVariable id: String): ModelAndView {
        val sensor = sensorDAO.get(id)
        try {
            sensorDAO.delete(sensor.id)
            mav.addObject("message", "Sensore eliminato con successo.")
        } catch (e: NullPointerException) {
            mav.addObject("message", "Sensore non trovato, riprova.")
        }
        mav.setViewName("redirect:/sensors")
        return mav
    }
}
