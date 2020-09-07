package it.srv.ThermoSmartSpring.repo;

import it.srv.ThermoSmartSpring.model.Orari_on_off;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface Orari_on_offRepository extends CrudRepository<Orari_on_off, Long> {
    List<Orari_on_off> findByRoomId(long room_id);

    Orari_on_off findById(long id);
}
