package it.srv.ThermoSmartSpring.dao;

import it.srv.ThermoSmartSpring.model.Sensor;
import it.srv.ThermoSmartSpring.repo.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SensorDAO {
    @Autowired
    private SensorRepository repo;

    public Iterable<Sensor> getAll() { return repo.findAll(); }

    public Sensor save(Sensor sensor) {
        return repo.save(sensor);
    }

    public Sensor get(String id) {
        return repo.findById(id).orElse(null);
    }

    public void delete(String id) { repo.deleteById(id); }

    public boolean exists(Sensor sensor){ return sensor != null; }

    public void update(Sensor sensor, String oldID){ repo.updateSensorById(sensor.getId(), sensor.getNome(), sensor.getLocation(), oldID);}

}