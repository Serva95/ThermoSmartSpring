package it.srv.ThermoSmartSpring.repo;

import it.srv.ThermoSmartSpring.model.Authorities;
import org.springframework.data.repository.CrudRepository;

public interface AuthoritiesRepository extends CrudRepository<Authorities, Short> {
    Authorities findByUsername(String username);
}
