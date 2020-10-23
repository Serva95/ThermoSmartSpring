package it.srv.ThermoSmartSpring.repo;

import it.srv.ThermoSmartSpring.model.Room;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<Room, Long> {
    Room findByIdOrderByNome(long id);
}
