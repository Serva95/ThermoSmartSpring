package it.srv.ThermoSmartSpring.dao;

import it.srv.ThermoSmartSpring.model.Info;
import it.srv.ThermoSmartSpring.repo.InfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InfoDAO {
    @Autowired
    InfoRepository repo;

    public Iterable<Info> getAll() { return repo.findAllByOrderByIdAsc(); }

    public Info get(String id) { return repo.findById(id).orElse(null); }
}
