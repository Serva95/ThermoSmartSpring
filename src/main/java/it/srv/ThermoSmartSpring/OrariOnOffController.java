package it.srv.ThermoSmartSpring;

import it.srv.ThermoSmartSpring.dao.OrariOnOffDAO;
import it.srv.ThermoSmartSpring.dto.OrariOnOffDTO;
import it.srv.ThermoSmartSpring.dto.OrariOnOffStringDTO;
import it.srv.ThermoSmartSpring.exception.BlankFieldsException;
import it.srv.ThermoSmartSpring.exception.InvalidFieldException;
import it.srv.ThermoSmartSpring.exception.ObjectAlreadyExistException;
import it.srv.ThermoSmartSpring.model.OrariOnOff;
import it.srv.ThermoSmartSpring.services.OrariOnOffService;
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

    @Autowired
    OrariOnOffService orariOnOffService;

    @GetMapping("/rooms/{id}/orarionoff")
    public ModelAndView orariOnOffs(ModelAndView mav, @PathVariable int id) {
        List<OrariOnOff> orariOnOffs = orariOnOffDAO.getByRoomId(id);
        getDaysOfWeek(mav);
        mav.addObject("orarionoffs", orariOnOffs);
        mav.setViewName("viewOrariOnOff");
        return mav;
    }

    @GetMapping("/rooms/{id}/orarionoff/new")
    public ModelAndView newOrariOnOffs(ModelAndView mav, @PathVariable int id) {
        OrariOnOffDTO orariOnOffDTO = new OrariOnOffDTO();
        for (short i = 0; i < 7; i++) {
            orariOnOffDTO.add(new OrariOnOffStringDTO(), i);
        }
        getDaysOfWeek(mav);
        mav.addObject("orarionoffDTO", orariOnOffDTO);
        mav.setViewName("newOrariOnOff");
        return mav;
    }

    @PostMapping("/rooms/{id}/orarionoff")
    public ModelAndView createOrariOnOff(
            ModelAndView mav,
            @PathVariable int id,
            @ModelAttribute("orarionoffDTO") final OrariOnOffDTO orariOnOffDTO) {
        try {
            orariOnOffService.saveOrari(orariOnOffDTO, id);
        } catch (BlankFieldsException | InvalidFieldException | ObjectAlreadyExistException e) {
            mav.addObject("message", e.getMessage());
            mav.addObject("orarionoffDTO", orariOnOffDTO);
            mav.setViewName("newOrariOnOff");
            getDaysOfWeek(mav);
            return mav;
        }
        mav.addObject("message", "Orari salvati con successo.");
        mav.setViewName("redirect:/rooms/"+id+"/orarionoff");
        return mav;
    }

    @GetMapping("/rooms/{id}/orarionoff/edit")
    public ModelAndView editOrariOnOff(ModelAndView mav, @PathVariable int id) {
        List<OrariOnOff> orariOnOffs = orariOnOffDAO.getByRoomId(id);
        OrariOnOffDTO orariOnOffDTO = new OrariOnOffDTO();
        orariOnOffDTO.orariToStringDTO(orariOnOffs);
        mav.addObject("orarionoffDTO", orariOnOffDTO);
        getDaysOfWeek(mav);
        mav.setViewName("editOrariOnOff");
        return mav;
    }

    @PutMapping("/rooms/{id}/orarionoff/edit")
    public ModelAndView updateOrariOnOff(
            ModelAndView mav,
            @PathVariable int id,
            @ModelAttribute("orarionoffDTO") final OrariOnOffDTO orariOnOffDTO) {
        try {
            orariOnOffService.updateOrari(orariOnOffDTO, id);
        } catch (BlankFieldsException | InvalidFieldException e) {
            mav.addObject("message", e.getMessage());
            mav.addObject("orarionoffDTO", orariOnOffDTO);
            mav.setViewName("editOrariOnOff");
            getDaysOfWeek(mav);
            return mav;
        }
        mav.addObject("message", "Orari modificati con successo.");
        mav.setViewName("redirect:/rooms/"+id+"/orarionoff");
        return mav;
    }

    private void getDaysOfWeek(ModelAndView mav) {
        ArrayList<String> days = new ArrayList<>();
        for (short i = 0; i < 7; i++) {
            days.add(DayOfWeek.of(i+1).getDisplayName(TextStyle.FULL, Locale.ITALY));
        }
        mav.addObject("days", days);
    }

}
