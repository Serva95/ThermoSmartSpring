package it.srv.ThermoSmartSpring.dao

import it.srv.ThermoSmartSpring.exception.InvalidAuthorityException
import it.srv.ThermoSmartSpring.model.Authorities
import it.srv.ThermoSmartSpring.repo.AuthoritiesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.ArrayList

@Service
@Transactional
open class AuthoritiesDAO {
    @Autowired
    lateinit var repo: AuthoritiesRepository

    @Throws(InvalidAuthorityException::class)
    open fun save(auto: Authorities): Authorities {
        if (auto.authority != "ROLE_ADMIN" && auto.authority != "ROLE_USER") {
            throw InvalidAuthorityException("Invalid authority - only ROLE_ADMIN or ROLE_USER are possible values")
        }
        return repo.save(auto)
    }

    open fun getByUsername(uname: String): ArrayList<Authorities> = repo.findByUsername(uname)

    open fun delete(id: Short) { repo.deleteById(id) }

    open fun getByAuthority(auth: String): ArrayList<Authorities> = repo.findByAuthority(auth)
}
