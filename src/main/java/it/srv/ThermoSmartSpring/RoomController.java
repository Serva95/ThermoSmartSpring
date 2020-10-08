package it.srv.ThermoSmartSpring;

import it.srv.ThermoSmartSpring.dao.RoomDAO;
import it.srv.ThermoSmartSpring.dao.SensorDAO;
import it.srv.ThermoSmartSpring.exception.BlankFieldsException;
import it.srv.ThermoSmartSpring.exception.InvalidFieldException;
import it.srv.ThermoSmartSpring.model.Room;
import it.srv.ThermoSmartSpring.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class RoomController {

    @Autowired
    RoomDAO roomDAO;

    @Autowired
    SensorDAO sensorDAO;

    @Autowired
    RoomService roomService;

    @GetMapping("/rooms")
    public ModelAndView Rooms(ModelAndView mav) {
        mav.setViewName("rooms");
        mav.addObject("rooms", roomDAO.getAll());
        return mav;
    }

    @GetMapping("/rooms/new")
    public ModelAndView newRoom(ModelAndView mav) {
        mav.addObject("room", new Room());
        mav.addObject("sensors", sensorDAO.findAllRoomIsNull());
        mav.setViewName("newRoom");
        return mav;
    }

    @PostMapping("/rooms/new")
    public ModelAndView createRoom(ModelAndView mav, @ModelAttribute("room") final Room room) {
        try {
            roomService.saveNewRoom(room);
        } catch (BlankFieldsException | InvalidFieldException e) {
            mav.addObject("message", e.getMessage());
            mav.addObject("room", room);
            mav.addObject("sensors", sensorDAO.findAllRoomIsNull());
            mav.setViewName("newRoom");
            return mav;
        }
        mav.addObject("message", "Stanza creata con successo.");
        mav.setViewName("redirect:/rooms");
        return mav;
    }

    @GetMapping("/rooms/{id}")
    public ModelAndView viewRoom(ModelAndView mav, @PathVariable int id) {
        return mav;
    }

    @GetMapping("/rooms/{id}/edit")
    public ModelAndView editRoom(ModelAndView mav, @PathVariable int id) {
        return mav;
    }

    @PutMapping("/rooms/{id}")
    public ModelAndView updateRoom(ModelAndView mav, @PathVariable int id) {
        return mav;
    }

    @DeleteMapping("/rooms/{id}")
    public ModelAndView deleteRoom(ModelAndView mav, @PathVariable int id) {
        return mav;
    }
}
