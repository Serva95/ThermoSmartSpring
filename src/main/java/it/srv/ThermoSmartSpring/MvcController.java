package it.srv.ThermoSmartSpring;

import it.srv.ThermoSmartSpring.dao.InfoDAO;
import it.srv.ThermoSmartSpring.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MvcController {

    @Autowired
    InfoDAO infoDAO;
    @Autowired
    UserDAO userDAO;

    @GetMapping("/")
    public ModelAndView viewHomePage(ModelAndView mav) {
        mav.setViewName("index");
        return mav;
    }

    @GetMapping("/debug")
    public ModelAndView debug(ModelAndView mav) {
        mav.addObject("infos", infoDAO.getAll());
        mav.addObject("users", userDAO.getAll());
        mav.setViewName("debug");
        return mav;
    }

}