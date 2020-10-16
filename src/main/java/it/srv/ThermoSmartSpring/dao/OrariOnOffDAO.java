package it.srv.ThermoSmartSpring.dao;

import it.srv.ThermoSmartSpring.model.OrariOnOff;
import it.srv.ThermoSmartSpring.repo.OrariOnOffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrariOnOffDAO {
    @Autowired
    private OrariOnOffRepository repo;

    public OrariOnOff save(OrariOnOff orariOnOff) { return repo.save(orariOnOff);}

    public void saveAll(List<OrariOnOff> orariOnOffs) { repo.saveAll(orariOnOffs);}

    public List<OrariOnOff> getByRoomId(long roomId) { return repo.findByRoom_Id(roomId); }

}
