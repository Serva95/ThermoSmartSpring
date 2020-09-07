package it.srv.ThermoSmartSpring.repo;


import it.srv.ThermoSmartSpring.model.Sensor;
import org.springframework.data.repository.CrudRepository;

public interface SensorRepository extends CrudRepository<Sensor, String> {
}