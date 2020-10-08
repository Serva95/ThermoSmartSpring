package it.srv.ThermoSmartSpring.dao;

import it.srv.ThermoSmartSpring.model.Temp;
import it.srv.ThermoSmartSpring.repo.TempRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TempDAO {
    @Autowired
    private TempRepository repo;

    public Temp save(Temp product) { return repo.save(product); }
}
