package it.srv.ThermoSmartSpring;

import it.srv.ThermoSmartSpring.dao.SensorDAO;
import it.srv.ThermoSmartSpring.dao.TempDAO;
import it.srv.ThermoSmartSpring.dto.AVGDTO;
import it.srv.ThermoSmartSpring.exception.InvalidFieldException;
import it.srv.ThermoSmartSpring.model.Temp;
import it.srv.ThermoSmartSpring.services.TempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
@RequestMapping("/api/temps")
public class TempControllerAPI {

    @Autowired
    TempDAO tempDAO;

    @Autowired
    SensorDAO sensorDAO;

    @Autowired
    TempService tempService;

    /*
    @Autowired
    public void setTempDAO(TempDAO tempDAO) { this.tempDAO = tempDAO; }

    @Autowired
    public void setSensorDAO(SensorDAO sensorDAO) { this.sensorDAO = sensorDAO; }

    @Autowired
    public void setTempService(TempService tempService) { this.tempService = tempService; }
     */

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, ArrayList<String>> getTempNMeds(@PathVariable String id){
        HashMap<String, ArrayList<String>> map = new HashMap<>();
        if (!sensorDAO.exists(id)){
            map.put("error", null);
            return map;
        }
        List<Temp> temps = tempService.getTempsClear(id);
        List<AVGDTO> avgs = tempDAO.findAVGVals(id, 3);
        Iterator<Temp> iter = temps.iterator();
        Iterator<AVGDTO> iter1 = avgs.iterator();
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
            LocalDate dt = LocalDate.parse(t.getGiorno());
            avgDates.add(dt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        }
        map.put("tempVals", tempsVal);
        map.put("times", times);
        map.put("tempsAVG", tempsAVG);
        map.put("avgDates", avgDates);
        return map;
    }

    @GetMapping(path = "/{id}/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> updateTemp(@PathVariable String id, @RequestParam(name = "previous") String previous){
        Temp last = tempDAO.findLastBySensor(id);
        HashMap<String, String> map = new HashMap<>();
        try {
            LocalTime prev = LocalTime.parse(previous);
            if (prev.isBefore(last.getCreatedAt().toLocalTime().truncatedTo(ChronoUnit.SECONDS))) {
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

    @GetMapping(path = "/{id}/updateMeds", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, ArrayList<String>> updateMeds(@PathVariable String id, @RequestParam(name = "days") int days){
        List<AVGDTO> avgs = tempDAO.findAVGVals(id, days);
        Iterator<AVGDTO> iter = avgs.iterator();
        HashMap<String, ArrayList<String>> map = new HashMap<>();
        ArrayList<String> tempsAVG = new ArrayList<>();
        ArrayList<String> avgDates = new ArrayList<>();
        while (iter.hasNext()) {
            AVGDTO t = iter.next();
            tempsAVG.add(t.getTemp().toString().substring(0,5));
            LocalDate dt = LocalDate.parse(t.getGiorno());
            avgDates.add(dt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        }
        map.put("tempsAVG", tempsAVG);
        map.put("avgDates", avgDates);
        return map;
    }

    @PostMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createTemp(@PathVariable String id, @RequestBody Map<String, String> data) {
        String apiKey = data.get("apikey");
        String temp = data.get("temp");
        try {
            tempService.save(id, temp, apiKey);
        } catch (InvalidFieldException e) {
            return new ResponseEntity<>("{\"status\": \""+e.getMessage()+"\"}", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("{\"status\": \"ok\"}", HttpStatus.CREATED);
    }
}
