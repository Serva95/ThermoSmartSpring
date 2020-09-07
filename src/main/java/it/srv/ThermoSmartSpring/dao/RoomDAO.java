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

    public Iterable<Room> listAll() {
        return repo.findAll();
    }

    public void save(Room product) {
        repo.save(product);
    }

    public Room get(long id) {
        return repo.findById(id);
    }

    public void delete(long id) {
        repo.deleteById(id);
    }
}
