package it.srv.ThermoSmartSpring;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MvcController {

    @GetMapping("/")
    public ModelAndView viewHomePage(ModelAndView mav) {
        mav.setViewName("index");
        return mav;
    }

}