package it.srv.ThermoSmartSpring;

import it.srv.ThermoSmartSpring.dao.OrariOnOffDAO;
import it.srv.ThermoSmartSpring.model.OrariOnOff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class OrariOnOffController {
    @Autowired
    OrariOnOffDAO orariOnOffDAO;

    @GetMapping("/rooms/{id}/orarionoff")
    public ModelAndView orariOnOffs(ModelAndView mav, @PathVariable int id) {
        List<OrariOnOff> orariOnOffs = orariOnOffDAO.getByRoomId(id);
        mav.addObject("orarionoffs", orariOnOffs);
        mav.setViewName("viewOrariOnOff");
        return mav;
    }

    @GetMapping("/rooms/{id}/orarionoff/new")
    public ModelAndView newOrariOnOffs(ModelAndView mav, @PathVariable int id) {
        List<OrariOnOff> orariOnOffs = orariOnOffDAO.getByRoomId(id);
        mav.addObject("orarionoffs", orariOnOffs);
        mav.setViewName("viewOrariOnOff");
        return mav;
    }

    @GetMapping("/rooms/{id}/orarionoff/{giorno}")
    public ModelAndView viewOrariOnOff(ModelAndView mav, @PathVariable int id) {
        mav.setViewName("editOrariOnOff");
        return mav;
    }

    @PostMapping("/rooms/{id}/orarionoff/{id}")
    public ModelAndView createOrariOnOff(ModelAndView mav, @PathVariable int id) {
        mav.addObject("message", "Stato modificato con successo.");
        mav.setViewName("redirect:/rooms/"+id);
        return mav;
    }

    @PutMapping("/rooms/{id}/orarionoff/{id}")
    public ModelAndView updateOrariOnOff(ModelAndView mav, @PathVariable long id) {
        mav.addObject("message", "Stato modificato con successo.");
        mav.setViewName("redirect:/rooms/"+id);
        return mav;
    }

}
