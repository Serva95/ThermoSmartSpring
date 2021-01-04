package it.srv.ThermoSmartSpring.repo;

import it.srv.ThermoSmartSpring.model.Info;
import org.springframework.data.repository.CrudRepository;

public interface InfoRepository extends CrudRepository<Info, String> {

    Iterable<Info> findAllByOrderByIdAsc();

}