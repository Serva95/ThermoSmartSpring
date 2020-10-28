package it.srv.ThermoSmartSpring.dao;

import it.srv.ThermoSmartSpring.dto.AVGDTO;
import it.srv.ThermoSmartSpring.model.Temp;
import it.srv.ThermoSmartSpring.repo.TempRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class TempDAO {
    @Autowired
    private TempRepository repo;

    public Temp save(Temp t) { return repo.save(t); }

    public Temp findLastBySensor(String sensorID){ return repo.findFirstBySensorIdOrderByCreatedAtDesc(sensorID);}

    public List<Temp> find150BySensor(String sensorID){
        List<Temp> temps = repo.findTop150BySensorIdOrderByCreatedAtDesc(sensorID);
        Collections.reverse(temps);
        return temps;
    }

    public List<AVGDTO> findAVGVals(String sensorID, int limit){
        List<AVGDTO> temps = repo.findAVGVals(sensorID, limit);
        Collections.reverse(temps);
        return temps;
    }
}
