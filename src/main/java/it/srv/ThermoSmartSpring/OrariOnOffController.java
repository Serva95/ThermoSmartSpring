package it.srv.ThermoSmartSpring;

import it.srv.ThermoSmartSpring.dao.OrariOnOffDAO;
import it.srv.ThermoSmartSpring.dto.OrariOnOffDTO;
import it.srv.ThermoSmartSpring.model.OrariOnOff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
        OrariOnOffDTO orariOnOffDTO = new OrariOnOffDTO();
        ArrayList<String> days = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            orariOnOffDTO.add(new OrariOnOff());
            days.add(DayOfWeek.of(i+1).getDisplayName(TextStyle.FULL, Locale.ITALY));
        }
        mav.addObject("days", days);
        mav.addObject("orarionoffDTO", orariOnOffDTO);
        mav.setViewName("newOrariOnOff");
        return mav;
    }

    @GetMapping("/rooms/{id}/orarionoff/{giorno}")
    public ModelAndView viewOrariOnOff(ModelAndView mav, @PathVariable int id, @PathVariable int giorno) {
        mav.setViewName("editOrariOnOff");
        return mav;
    }

    @PostMapping("/rooms/{id}/orarionoff")
    public ModelAndView createOrariOnOff(ModelAndView mav, @PathVariable int id) {
        mav.addObject("message", "Stato modificato con successo.");
        mav.setViewName("redirect:/rooms/"+id+"/orarionoff");
        return mav;
    }

    @PutMapping("/rooms/{id}/orarionoff/{giorno}")
    public ModelAndView updateOrariOnOff(ModelAndView mav, @PathVariable long id, @PathVariable int giorno) {
        mav.addObject("message", "Stato modificato con successo.");
        mav.setViewName("redirect:/rooms/"+id);
        return mav;
    }

}
