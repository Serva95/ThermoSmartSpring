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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

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
}
