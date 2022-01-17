package it.srv.ThermoSmartSpring.repo;

import it.srv.ThermoSmartSpring.model.Vmc;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalTime;

public interface VmcRepository extends CrudRepository<Vmc, String> {
    Vmc findFirstByOrderByIdDesc();

    @Modifying
    @Query("update Vmc v set v.id = ?1, v.impostazioneFunzione = ?2, v.statoAttuale = ?3, " +
            "v.programmedOnTime = ?4, v.programmedOffTime = ?5 where v.id = ?6")
    void updateVmcById(String id, boolean impostazioneFunzione, Boolean statoAttuale,
                          LocalTime programmedOnTime, LocalTime programmedOffTime, String oldID);
}
