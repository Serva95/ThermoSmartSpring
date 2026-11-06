package it.srv.ThermoSmartSpring.dao

import it.srv.ThermoSmartSpring.model.Vmc
import it.srv.ThermoSmartSpring.repo.VmcRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
open class VmcDAO {
    @Autowired
    lateinit var repo: VmcRepository

    open fun findFirst(): Vmc? = repo.findFirstByOrderByIdDesc()

    open fun save(vmc: Vmc): Vmc = repo.save(vmc)

    open fun get(id: String): Vmc = repo.findById(id).orElse(null) as Vmc

    open fun delete(id: String) { repo.deleteById(id) }

    open fun exists(id: String): Boolean {
        val vmc = get(id)
        return vmc != null
    }

    open fun update(vmc: Vmc, oldId: String) { repo.updateVmcById(vmc.id, vmc.impostazioneFunzione, vmc.statoAttuale, vmc.programmedOnTime, vmc.programmedOffTime, oldId) }
}
