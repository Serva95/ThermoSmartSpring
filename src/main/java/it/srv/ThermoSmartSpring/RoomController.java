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
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    RoomDAO roomDAO;

    @Autowired
    SensorDAO sensorDAO;

    @Autowired
    RoomService roomService;

    @GetMapping("")
    public ModelAndView Rooms(ModelAndView mav) {
        mav.setViewName("rooms");
        mav.addObject("rooms", roomDAO.getAll(true));
        return mav;
    }

    @GetMapping("/new")
    public ModelAndView newRoom(ModelAndView mav) {
        mav.addObject("room", new Room());
        mav.addObject("sensors", sensorDAO.findAllRoomIsNull());
        mav.setViewName("newRoom");
        return mav;
    }

    @PostMapping("/new")
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

    @GetMapping("/{id}")
    public ModelAndView viewRoom(ModelAndView mav, @PathVariable int id) {
        Room room = roomDAO.get(id);
        if (room == null) {
            mav.addObject("message", "Stanza non trovata, riprova.");
            mav.setViewName("redirect:/rooms");
            return mav;
        }
        mav.addObject("room", room);
        mav.setViewName("viewRoom");
        return mav;
    }

    @GetMapping("/{id}/edit")
    public ModelAndView editRoom(ModelAndView mav, @PathVariable int id) {
        Room room = roomDAO.get(id);
        if (room == null) {
            mav.addObject("message", "Stanza non trovata, riprova.");
            mav.setViewName("redirect:/rooms");
            return mav;
        }
        if(room.getSensor() != null){
            mav.addObject("sensors", sensorDAO.findNotAssociatedPlusActual(room.getSensor().getId()));
        } else {
            mav.addObject("sensors", sensorDAO.findAllRoomIsNull());
        }
        mav.addObject("room", room);
        mav.setViewName("editRoom");
        return mav;
    }

    @PutMapping("/{id}")
    public ModelAndView updateRoom(
            ModelAndView mav, @PathVariable int id, @ModelAttribute("room") final Room room) {
        Room oldRoom = roomDAO.get(id);
        if (oldRoom == null) {
            mav.addObject("message", "Stanza non trovata, riprova.");
            mav.setViewName("redirect:/rooms");
            return mav;
        }
        try {
            roomService.updateRoom(oldRoom, room);
        } catch (BlankFieldsException | InvalidFieldException e) {
            mav.addObject("message", e.getMessage());
            mav.addObject("room", room);
            if(room.getSensor() != null){
                mav.addObject("sensors", sensorDAO.findNotAssociatedPlusActual(oldRoom.getSensor().getId()));
            } else {
                mav.addObject("sensors", sensorDAO.findAllRoomIsNull());
            }
            mav.setViewName("editRoom");
            return mav;
        }
        mav.addObject("message", "Stanza modificata con successo.");
        mav.setViewName("redirect:/rooms");
        return mav;
    }

    @PutMapping("/{id}/manualActive")
    public ModelAndView updateRoomManAct(ModelAndView mav, @PathVariable int id) {
        Room room = roomDAO.get(id);
        if (room == null) {
            mav.addObject("message", "Stanza non trovata, riprova.");
            mav.setViewName("redirect:/rooms");
            return mav;
        }
        roomService.updateRoomStatus(room, true, false, false);
        mav.addObject("message", "Stato modificato con successo.");
        mav.setViewName("redirect:/rooms/"+id);
        return mav;
    }

    @PutMapping("/{id}/manualInactive")
    public ModelAndView updateRoomManInact(ModelAndView mav, @PathVariable int id) {
        Room room = roomDAO.get(id);
        if (room == null) {
            mav.addObject("message", "Stanza non trovata, riprova.");
            mav.setViewName("redirect:/rooms");
            return mav;
        }
        roomService.updateRoomStatus(room, false, true, false);
        mav.addObject("message", "Stato modificato con successo.");
        mav.setViewName("redirect:/rooms/"+id);
        return mav;
    }

    @PutMapping("/{id}/manualOff")
    public ModelAndView updateRoomManOff(ModelAndView mav, @PathVariable int id) {
        Room room = roomDAO.get(id);
        if (room == null) {
            mav.addObject("message", "Stanza non trovata, riprova.");
            mav.setViewName("redirect:/rooms");
            return mav;
        }
        roomService.updateRoomStatus(room, false, false, true);
        mav.addObject("message", "Stato modificato con successo.");
        mav.setViewName("redirect:/rooms/"+id);
        return mav;
    }

    @DeleteMapping("/{id}")
    public ModelAndView deleteRoom(ModelAndView mav, @PathVariable int id) {
        Room room = roomDAO.get(id);
        try {
            roomDAO.delete(room.getId());
        } catch (NullPointerException e) {
            mav.addObject("message", "Stanza non trovata, riprova.");
        }
        mav.addObject("message", "Stanza eliminata con successo.");
        mav.setViewName("redirect:/rooms");
        return mav;
    }
}
