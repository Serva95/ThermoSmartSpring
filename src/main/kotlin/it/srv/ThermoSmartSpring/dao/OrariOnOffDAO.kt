package it.srv.ThermoSmartSpring.dao

import it.srv.ThermoSmartSpring.model.OrariOnOff
import it.srv.ThermoSmartSpring.repo.OrariOnOffRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
open class OrariOnOffDAO {
    @Autowired
    lateinit var repo: OrariOnOffRepository

    open fun save(orariOnOff: OrariOnOff): OrariOnOff = repo.save(orariOnOff)

    open fun saveAll(orariOnOffs: List<OrariOnOff>) { repo.saveAll(orariOnOffs) }

    open fun getByRoomId(roomId: Long): List<OrariOnOff> = repo.findByRoom_IdOrderByGiorno(roomId)
}
