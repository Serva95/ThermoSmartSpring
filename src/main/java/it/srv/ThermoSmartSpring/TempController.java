package it.srv.ThermoSmartSpring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class TempController {

    @GetMapping("/temps")
    public ModelAndView Temps(ModelAndView mav) {
        return mav;
    }

    @GetMapping("/temps/{id}")
    public ModelAndView viewTemp(ModelAndView mav, @PathVariable int id) {
        return mav;
    }

    @PostMapping("/temps/{id}")
    public ModelAndView createTemp(ModelAndView mav, @PathVariable int id) {
        return mav;
    }
}
