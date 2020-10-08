package it.srv.ThermoSmartSpring.repo;

import it.srv.ThermoSmartSpring.model.Sensor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SensorRepository extends CrudRepository<Sensor, String> {
    @Modifying
    @Query("update Sensor s set s.id = ?1, s.nome = ?2, s.location = ?3 where s.id = ?4")
    void updateSensorById(String id, String nome, String location, String oldID);

    Iterable<Sensor> findAllByRoom_SensorIsNull();

    //@Query("SELECT s.id, s.nome, s.location FROM Sensor AS s LEFT OUTER JOIN Room AS r WHERE r.sensor.id IS NULL OR r.sensor.id = ?1")
    Iterable<Sensor> findAllByRoom_SensorIsNullOrIdEquals(String id);

}