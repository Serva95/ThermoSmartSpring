package it.srv.ThermoSmartSpring.repo;

import it.srv.ThermoSmartSpring.model.Vmc;
import org.springframework.data.repository.CrudRepository;

public interface VmcRepository extends CrudRepository<Vmc, String> {
    Vmc findFirstByOrderByIdDesc();
}
