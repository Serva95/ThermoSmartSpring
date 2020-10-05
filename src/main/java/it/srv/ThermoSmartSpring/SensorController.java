package it.srv.ThermoSmartSpring;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class SensorController {

    @GetMapping("/sensors")
    public ModelAndView sensors(ModelAndView mav) {
        return mav;
    }

    @GetMapping("/sensors/{id}")
    public ModelAndView viewSensor(ModelAndView mav, @PathVariable int id) {
        return mav;
    }

    @PostMapping("/sensors/{id}")
    public ModelAndView createSensor(ModelAndView mav, @PathVariable int id) {
        return mav;
    }

    @PutMapping("/sensors/{id}")
    public ModelAndView updateSensor(ModelAndView mav, @PathVariable int id) {
        return mav;
    }

    @DeleteMapping("/sensors/{id}")
    public ModelAndView deleteSensor(ModelAndView mav, @PathVariable int id) {
        return mav;
    }
}
