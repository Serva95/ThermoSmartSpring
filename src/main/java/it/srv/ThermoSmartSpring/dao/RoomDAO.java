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

    public Iterable<Room> getAll() {
        return repo.findAll();
    }

    public Room save(Room product) { return repo.save(product); }

    public Room get(long id) {
        return repo.findById(id);
    }

    public void delete(long id) {
        repo.deleteById(id);
    }

    public boolean exists(Room room){ return room != null; }
}
