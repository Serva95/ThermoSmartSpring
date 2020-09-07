package it.srv.ThermoSmartSpring.repo;

import it.srv.ThermoSmartSpring.model.Temp;
import org.springframework.data.repository.CrudRepository;

public interface TempRepository extends CrudRepository<Temp, Long> {
    Temp findFirstBySensorIdOrderByCreatedAtDesc(String sensorId);

}