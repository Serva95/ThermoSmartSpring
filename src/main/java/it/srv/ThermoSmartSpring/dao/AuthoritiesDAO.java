package it.srv.ThermoSmartSpring.dao;

import it.srv.ThermoSmartSpring.exception.InvalidAuthorityException;
import it.srv.ThermoSmartSpring.model.Authorities;
import it.srv.ThermoSmartSpring.repo.AuthoritiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
public class AuthoritiesDAO {
    @Autowired
    private AuthoritiesRepository repo;

    public Authorities save(Authorities auto) throws InvalidAuthorityException {
        if (!(auto.getAuthority().equals("ROLE_ADMIN") || auto.getAuthority().equals("ROLE_USER"))){
            throw new InvalidAuthorityException("Invalid authority - only ROLE_ADMIN or ROLE_USER are possible values");
        }
        return repo.save(auto);
    }

    public ArrayList<Authorities> getByUsername(String uname) { return repo.findByUsername(uname); }

    public void delete(short id) { repo.deleteById(id); }
}
