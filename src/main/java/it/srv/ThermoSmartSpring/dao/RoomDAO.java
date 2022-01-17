package it.srv.ThermoSmartSpring.dao;

import it.srv.ThermoSmartSpring.model.Room;
import it.srv.ThermoSmartSpring.repo.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoomDAO {
    @Autowired
    private RoomRepository repo;

    public Iterable<Room> getAll(Boolean asc) { return asc ? repo.findAllByOrderByNomeAsc() : repo.findAllByOrderByNomeDesc(); }

    public Room save(Room room) { return repo.save(room); }

    public Room get(long id) { return repo.findById(id); }

    public Room getBySensorId(String id) { return repo.findBySensor_Id(id); }

    public void delete(long id) {
        repo.deleteById(id);
    }

    public boolean exists(Room room){ return room != null; }
}
