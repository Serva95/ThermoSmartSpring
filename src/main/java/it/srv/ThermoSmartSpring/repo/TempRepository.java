package it.srv.ThermoSmartSpring.repo;

import it.srv.ThermoSmartSpring.dto.AVGDTO;
import it.srv.ThermoSmartSpring.model.Temp;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TempRepository extends CrudRepository<Temp, Long> {
    Temp findFirstBySensorIdOrderByCreatedAtDesc(String sensorId);

    List<Temp> findTop150BySensorIdOrderByCreatedAtDesc(String sensorid);

    @Query(value = "SELECT CAST(createdat AS DATE) AS giorno, AVG(temp) AS temp FROM temps WHERE sensorid = ?1 GROUP BY giorno ORDER BY giorno DESC LIMIT ?2", nativeQuery = true)
    List<AVGDTO> findAVGVals(String id, int limit);

    List<Temp> findBySensorIdOrderByCreatedAtDesc(String sensorID, Pageable pageable);

}