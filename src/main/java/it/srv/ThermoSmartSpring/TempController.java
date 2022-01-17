package it.srv.ThermoSmartSpring;

import it.srv.ThermoSmartSpring.dao.RoomDAO;
import it.srv.ThermoSmartSpring.dao.SensorDAO;
import it.srv.ThermoSmartSpring.dao.TempDAO;
import it.srv.ThermoSmartSpring.model.Room;
import it.srv.ThermoSmartSpring.model.Temp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/temps")
public class TempController {

    @Autowired
    TempDAO tempDAO;

    @Autowired
    RoomDAO roomDAO;

    @Autowired
    SensorDAO sensorDAO;

    /*
    @Autowired
    public void setRoomDAO(RoomDAO roomDAO) { this.roomDAO = roomDAO; }

    @Autowired
    public void setTempDAO(TempDAO tempDAO) { this.tempDAO = tempDAO; }

    @Autowired
    public void setSensorDAO(SensorDAO sensorDAO) { this.sensorDAO = sensorDAO; }
     */

    @GetMapping("")
    public ModelAndView Temps(ModelAndView mav) {
        mav.setViewName("temps");
        Map<String, String> lastTemps = new HashMap<>();
        Map<String, Boolean> sensorReadError = new HashMap<>();
        Iterable<Room> rooms = roomDAO.getAll(true);
        rooms.iterator().forEachRemaining((t) -> {
            if (t.getSensor() != null){
                Temp tm = tempDAO.findLastBySensor(t.getSensor().getId());
                if(tm!=null){
                    sensorReadError.put(t.getSensor().getId(), tm.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(31)));
                    lastTemps.put(t.getSensor().getId(), tm.getTemp().toString()+"°C il: "+tm.getCreatedDateTimeFormatted());
                }}});
        mav.addObject("sensorReadError", sensorReadError);
        mav.addObject("lastTemps", lastTemps);
        mav.addObject("rooms", rooms);
        return mav;
    }

    @GetMapping("/{id}")
    public ModelAndView viewTemp(ModelAndView mav, @PathVariable String id) {
        if (!sensorDAO.exists(id)){
            mav.addObject("sensor", false);
        }else {
            Temp last = tempDAO.findLastBySensor(id);
            Room room = roomDAO.getBySensorId(id);
            mav.addObject("room", room);
            mav.addObject("last", last);
        }
        mav.setViewName("viewTemps");
        return mav;
    }

}
