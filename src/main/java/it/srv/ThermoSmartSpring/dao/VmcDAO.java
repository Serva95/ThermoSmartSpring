package it.srv.ThermoSmartSpring.dao;

import it.srv.ThermoSmartSpring.model.Vmc;
import it.srv.ThermoSmartSpring.repo.VmcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VmcDAO {
    @Autowired
    private VmcRepository repo;

    public Vmc findFirst(){ return repo.findFirstByOrderByIdDesc();}

    public Vmc save(Vmc vmc) { return repo.save(vmc); }

    public Vmc get(String id) { return repo.findById(id).orElse(null); }

    public void delete(String id) {
        repo.deleteById(id);
    }
}
