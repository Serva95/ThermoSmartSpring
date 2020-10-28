package it.srv.ThermoSmartSpring;

import it.srv.ThermoSmartSpring.dao.RoomDAO;
import it.srv.ThermoSmartSpring.dao.SensorDAO;
import it.srv.ThermoSmartSpring.dao.TempDAO;
import it.srv.ThermoSmartSpring.dto.AVGDTO;
import it.srv.ThermoSmartSpring.model.Room;
import it.srv.ThermoSmartSpring.model.Temp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestController
public class TempController {

    @Autowired
    TempDAO tempDAO;

    @Autowired
    RoomDAO roomDAO;

    @Autowired
    SensorDAO sensorDAO;

    @GetMapping("/temps")
    public ModelAndView Temps(ModelAndView mav) {
        mav.setViewName("temps");
        Iterable<Room> rooms = roomDAO.getAll();
        mav.addObject("rooms", rooms);
        return mav;
    }

    @GetMapping("/temps/{id}")
    public ModelAndView viewTemp(ModelAndView mav, @PathVariable String id) {
        if (!sensorDAO.exists(id)){
            mav.addObject("sensor", false);
        }else {
            Temp last = tempDAO.findLastBySensor(id);
            mav.addObject("last", last);
        }
        mav.setViewName("viewTemps");
        return mav;
    }

    @GetMapping(path = "/api/temps/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, ArrayList<String>> getTempNMeds(@PathVariable String id){
        List<Temp> temps = tempDAO.find150BySensor(id);
        List<AVGDTO> avgs = tempDAO.findAVGVals(id, 7);
        Iterator<Temp> iter = temps.iterator();
        Iterator<AVGDTO> iter1 = avgs.iterator();
        HashMap<String, ArrayList<String>> map = new HashMap<>();
        ArrayList<String> tempsVal = new ArrayList<>();
        ArrayList<String> times = new ArrayList<>();
        while (iter.hasNext()) {
            Temp t = iter.next();
            tempsVal.add(t.getTemp().toString());
            times.add(t.getCreatedTimeFormatted());
        }
        ArrayList<String> tempsAVG = new ArrayList<>();
        ArrayList<String> avgDates = new ArrayList<>();
        while (iter1.hasNext()) {
            AVGDTO t = iter1.next();
            tempsAVG.add(t.getTemp().toString().substring(0,5));
            LocalDate dt = t.getGiorno();
            avgDates.add(dt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        }
        map.put("tempVals", tempsVal);
        map.put("times", times);
        map.put("tempsAVG", tempsAVG);
        map.put("avgDates", avgDates);
        return map;
    }

    @GetMapping(path = "/api/temps/{id}/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> updateTemp(@PathVariable String id, @RequestParam(name = "previous") String previous){
        Temp last = tempDAO.findLastBySensor(id);
        HashMap<String, String> map = new HashMap<>();
        try {
            LocalTime prev = LocalTime.parse(previous);
            if (prev.isBefore(last.getCreatedAt().toLocalTime())) {
                String temp = last.getTemp().toString();
                String time = last.getCreatedTimeFormatted();
                map.put("temp", temp);
                map.put("created", time);
            }
            return map;
        }catch (DateTimeParseException ignored){
            return map;
        }
    }

    @GetMapping(path = "/api/temps/{id}/updateMeds", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, ArrayList<String>> updateMeds(@PathVariable String id, @RequestParam(name = "days") int days){
        List<AVGDTO> avgs = tempDAO.findAVGVals(id, days);
        Iterator<AVGDTO> iter = avgs.iterator();
        HashMap<String, ArrayList<String>> map = new HashMap<>();
        ArrayList<String> tempsAVG = new ArrayList<>();
        ArrayList<String> avgDates = new ArrayList<>();
        while (iter.hasNext()) {
            AVGDTO t = iter.next();
            tempsAVG.add(t.getTemp().toString().substring(0,5));
            LocalDate dt = t.getGiorno();
            avgDates.add(dt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        }
        map.put("tempsAVG", tempsAVG);
        map.put("avgDates", avgDates);
        return map;
    }

    @PostMapping("/temps/{id}")
    public ModelAndView createTemp(ModelAndView mav, @PathVariable String id) {
        return mav;
    }
}
