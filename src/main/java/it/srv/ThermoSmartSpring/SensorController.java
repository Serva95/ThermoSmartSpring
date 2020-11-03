package it.srv.ThermoSmartSpring;

import it.srv.ThermoSmartSpring.dao.SensorDAO;
import it.srv.ThermoSmartSpring.exception.BlankFieldsException;
import it.srv.ThermoSmartSpring.exception.ObjectAlreadyExistException;
import it.srv.ThermoSmartSpring.model.Sensor;
import it.srv.ThermoSmartSpring.services.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class SensorController {
    @Autowired
    SensorDAO sensorDAO;

    @Autowired
    SensorService sensorService;

    @GetMapping("/sensors")
    public ModelAndView sensors(ModelAndView mav) {
        mav.setViewName("sensors");
        Iterable<Sensor> sensors = sensorDAO.getAll(true);
        mav.addObject("sensors", sensors);
        return mav;
    }

    @GetMapping("/sensors/new")
    public ModelAndView newSensor(ModelAndView mav) {
        Sensor sensor = new Sensor();
        mav.addObject("sensor", sensor);
        mav.setViewName("newSensor");
        return mav;
    }

    @PostMapping("/sensors/new")
    public ModelAndView createSensor(ModelAndView mav, @ModelAttribute("sensor") final Sensor sensor) {
        try {
            Sensor newSensor = sensorService.saveNewSensor(sensor);
        } catch (ObjectAlreadyExistException | BlankFieldsException e) {
            if (e.getClass().equals(ObjectAlreadyExistException.class)) {
                sensor.setId("");
            }
            mav.addObject("message", e.getMessage());
            mav.addObject("sensor", sensor);
            mav.setViewName("newSensor");
            return mav;
        }
        mav.addObject("message", "Sensore inserito con successo.");
        mav.setViewName("redirect:/sensors");
        return mav;
    }

    @GetMapping("/sensors/{id}")
    public ModelAndView editSensor(ModelAndView mav, @PathVariable String id) {
        mav.addObject("sensor", sensorDAO.get(id));
        mav.setViewName("editSensor");
        return mav;
    }

    @PutMapping("/sensors/{id}")
    public ModelAndView updateSensor(ModelAndView mav, @PathVariable String id, @ModelAttribute("sensor") final Sensor newData) {
        Sensor sensor = sensorDAO.get(id);
        if (!sensorDAO.exists(sensor)){
            mav.addObject("message", "Sensore non trovato, riprova.");
            mav.setViewName("redirect:/sensors");
            return mav;
        }
        try {
            sensorService.updateSensor(sensor, newData);
        } catch (BlankFieldsException | ObjectAlreadyExistException e) {
            mav.addObject("sensor", sensor);
            mav.setViewName("editSensor");
            mav.addObject("message", e.getMessage());
            return mav;
        }
        mav.setViewName("redirect:/sensors");
        return mav;
    }

    @DeleteMapping("/sensors/{id}")
    public ModelAndView deleteSensor(ModelAndView mav, @PathVariable String id) {
        Sensor sensor = sensorDAO.get(id);
        try {
            sensorDAO.delete(sensor.getId());
        } catch (NullPointerException e){
            mav.addObject("message", "Sensore non trovato, riprova.");
        }
        mav.addObject("message", "Sensore eliminato con successo.");
        mav.setViewName("redirect:/sensors");
        return mav;
    }
}
