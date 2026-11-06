package it.srv.ThermoSmartSpring.dao

import it.srv.ThermoSmartSpring.model.Info
import it.srv.ThermoSmartSpring.repo.InfoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
open class InfoDAO {
    @Autowired
    lateinit var repo: InfoRepository

    open fun getAll(): Iterable<Info> = repo.findAllByOrderByIdAsc()

    open fun get(id: String): Info = repo.findById(id).orElse(null) as Info
}
