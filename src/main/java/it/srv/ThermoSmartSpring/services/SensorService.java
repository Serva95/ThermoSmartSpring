package it.srv.ThermoSmartSpring.services;

import it.srv.ThermoSmartSpring.dao.SensorDAO;
import it.srv.ThermoSmartSpring.exception.BlankFieldsException;
import it.srv.ThermoSmartSpring.exception.ObjectAlreadyExistException;
import it.srv.ThermoSmartSpring.model.Sensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SensorService {
    @Autowired
    SensorDAO sensorDAO;

    public Sensor saveNewSensor(final Sensor sensor) throws ObjectAlreadyExistException, BlankFieldsException {
        if(sensor.getId().isEmpty() || sensor.getLocation().isEmpty() || sensor.getNome().isEmpty()){
            throw new BlankFieldsException("Completa tutti i campi");
        }
        if(sensorDAO.exists(sensorDAO.get(sensor.getId()))){
            throw new ObjectAlreadyExistException("Esiste già un sensore con questo id, provane uno diverso.");
        }
        return sensorDAO.save(sensor);
    }

    public Sensor updateSensor(final Sensor oldSensor, Sensor newData) throws BlankFieldsException, ObjectAlreadyExistException {
        if(newData.getId().isEmpty() || newData.getLocation().isEmpty() || newData.getNome().isEmpty()){
            throw new BlankFieldsException("Completa tutti i campi");
        }
        if(!oldSensor.getId().equalsIgnoreCase(newData.getId()) && sensorDAO.exists(sensorDAO.get(newData.getId()))){
            throw new ObjectAlreadyExistException("Esiste già un sensore con questo id, provane uno diverso.");
        }
        if (!oldSensor.getId().equals(newData.getId())){
            sensorDAO.update(newData, oldSensor.getId());
        } else {
            sensorDAO.save(newData);
        }
        return newData;
    }
}
