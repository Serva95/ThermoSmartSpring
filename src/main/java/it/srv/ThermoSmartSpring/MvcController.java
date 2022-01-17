package it.srv.ThermoSmartSpring;

import it.srv.ThermoSmartSpring.dao.InfoDAO;
import it.srv.ThermoSmartSpring.dao.UserDAO;
import it.srv.ThermoSmartSpring.model.Info;
import it.srv.ThermoSmartSpring.model.Temp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

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

    @GetMapping(path = "/api/valveStatus", produces = MediaType.APPLICATION_JSON_VALUE)
    public String valveStatus(){
        Info info = infoDAO.get("status");
        return info.getValue();
    }

}