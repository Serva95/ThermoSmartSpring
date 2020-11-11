package it.srv.ThermoSmartSpring.services;

import it.srv.ThermoSmartSpring.dao.SensorDAO;
import it.srv.ThermoSmartSpring.dao.TempDAO;
import it.srv.ThermoSmartSpring.exception.InvalidFieldException;
import it.srv.ThermoSmartSpring.model.Sensor;
import it.srv.ThermoSmartSpring.model.Temp;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional
public class TempService {
    @Autowired
    TempDAO tempDAO;

    @Autowired
    SensorDAO sensorDAO;

    /**
     * @param sensorID id del sensore che genera i dati
     * @param temp temperatura con 2 numeri significativi
     * @param apikey sha256(temp+id) server come checksum e come sicurezza nella fase di salvataggio dati
     * @throws InvalidFieldException se l'id è non valido o se il valore di apikey non è corretto
     */
    public void save(String sensorID, String temp, String apikey) throws InvalidFieldException {
        MessageDigest digest = null;
        String tosha = temp+sensorID;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ignored) { }
        byte[] hash = digest.digest(
                tosha.getBytes(StandardCharsets.UTF_8));
        String sha256hex = new String(Hex.encode(hash));
        if(sha256hex.equals(apikey)){
            Temp t = new Temp();
            if(!sensorDAO.exists(sensorID))
                throw new InvalidFieldException("Id sensore non valido");
            Sensor s = sensorDAO.get(sensorID);
            t.setSensor(s);
            t.setTemp(BigDecimal.valueOf(Double.parseDouble(temp)));
            t.setCreatedAt(LocalDateTime.now());
            tempDAO.save(t);
        } else
            throw new InvalidFieldException("apiKey non valida");
    }

    public List<Temp> getTempsClear(String sensorId){
        List<Temp> temps = tempDAO.findLastDayBySensor(750, sensorId);
        if (temps.size()<200)
            return temps;
        List<Temp> toret = new ArrayList<>();
        Iterator<Temp> iter = temps.iterator();
        while (iter.hasNext()){
            Temp avg = new Temp();
            BigDecimal tmp = new BigDecimal(0);
            for (int i = 0; i < 5; i++) {
                if (iter.hasNext()) {
                    Temp next = iter.next();
                    avg.setSensor(next.getSensor());
                    avg.setCreatedAt(next.getCreatedAt());
                    avg.setId(next.getId());
                    tmp = tmp.add(next.getTemp());
                } else
                    tmp = tmp.divide(new BigDecimal(i), 2, RoundingMode.HALF_UP);
                if (i==4)
                    tmp = tmp.divide(new BigDecimal(5), 2, RoundingMode.HALF_UP);
            }
            avg.setTemp(tmp);
            toret.add(avg);
        }
        return toret;
    }
}
