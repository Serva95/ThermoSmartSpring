package it.srv.ThermoSmartSpring;

import it.srv.ThermoSmartSpring.dao.RoomDAO;
import it.srv.ThermoSmartSpring.dao.TempDAO;
import it.srv.ThermoSmartSpring.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class TempController {

    @Autowired
    TempDAO tempDAO;

    @Autowired
    RoomDAO roomDAO;

    @GetMapping("/temps")
    public ModelAndView Temps(ModelAndView mav) {
        mav.setViewName("temps");
        Iterable<Room> rooms = roomDAO.getAll();
        mav.addObject("rooms", rooms);
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
