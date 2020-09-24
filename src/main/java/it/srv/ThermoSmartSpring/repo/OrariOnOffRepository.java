package it.srv.ThermoSmartSpring.repo;

import it.srv.ThermoSmartSpring.model.OrariOnOff;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrariOnOffRepository extends CrudRepository<OrariOnOff, Long> {
    List<OrariOnOff> findByRoomId(long room_id);

    OrariOnOff findById(long id);
}