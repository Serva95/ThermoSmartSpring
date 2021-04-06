package it.srv.ThermoSmartSpring.repo;

import it.srv.ThermoSmartSpring.model.Room;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<Room, Long> {
    Room findById(long id);

    Room findBySensor_Id(String id);

    Iterable<Room> findAllByOrderByNomeAsc();

    Iterable<Room> findAllByOrderByNomeDesc();
}
