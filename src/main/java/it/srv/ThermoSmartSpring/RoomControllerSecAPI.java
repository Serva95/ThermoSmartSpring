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
@RequestMapping("/secure-api/rooms")
public class RoomControllerSecAPI {

    @Autowired
    RoomDAO roomDAO;

    @Autowired
    SensorDAO sensorDAO;

    @Autowired
    RoomService roomService;

    @PutMapping("")
    public String allManualActive(@RequestParam(name = "status") String status) {
        String msg = "Si è verificato un errore, riprova";
        if(status.equalsIgnoreCase("on")){
            if (roomService.updateAllRooms(status))
                msg = "Tutte le stanze sono ora in modalità manuale massimo";
        } else if (status.equalsIgnoreCase("off")){
            if (roomService.updateAllRooms(status))
                msg = "Tutte le stanze sono ora in modalità automatica secondo gli orari";
        } else if (status.equalsIgnoreCase("alloff")){
            if (roomService.updateAllRooms(status))
                msg = "Il controllo è ora spento in tutte le stanze";
        }
        return msg;
    }
}
