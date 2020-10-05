package it.srv.ThermoSmartSpring;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class VmcController {

    @GetMapping("/vmcs")
    public ModelAndView vmcs(ModelAndView mav) {
        return mav;
    }

    @GetMapping("/vmcs/{id}")
    public ModelAndView viewVMC(ModelAndView mav, @PathVariable int id) {
        return mav;
    }

    @PostMapping("/vmcs/{id}")
    public ModelAndView createVMC(ModelAndView mav, @PathVariable int id) {
        return mav;
    }

    @PutMapping("/vmcs/{id}")
    public ModelAndView updateVMC(ModelAndView mav, @PathVariable int id) {
        return mav;
    }

    @DeleteMapping("/vmcs/{id}")
    public ModelAndView deleteVMC(ModelAndView mav, @PathVariable int id) {
        return mav;
    }
}
