package it.srv.ThermoSmartSpring;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class OrariOnOffController {

    @GetMapping("/orarionoffs")
    public ModelAndView orariOnOffs(ModelAndView mav) {
        return mav;
    }

    @GetMapping("/orarionoffs/{id}")
    public ModelAndView viewOrariOnOff(ModelAndView mav, @PathVariable int id) {
        return mav;
    }

    @PostMapping("/orarionoffs/{id}")
    public ModelAndView createOrariOnOff(ModelAndView mav, @PathVariable int id) {
        return mav;
    }

    @PutMapping("/orarionoffs/{id}")
    public ModelAndView updateOrariOnOff(ModelAndView mav, @PathVariable int id) {
        return mav;
    }

    @DeleteMapping("/orarionoffs/{id}")
    public ModelAndView deleteOrariOnOff(ModelAndView mav, @PathVariable int id) {
        return mav;
    }
}
