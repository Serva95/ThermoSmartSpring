package it.srv.ThermoSmartSpring.dao

import it.srv.ThermoSmartSpring.model.User
import it.srv.ThermoSmartSpring.repo.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
open class UserDAO {
    @Autowired
    lateinit var repo: UserRepository

    open fun save(user: User): User = repo.save(user)

    open fun getById(id: Long): User? = repo.findById(id).orElse(null)

    open fun getAll(): Iterable<User> = repo.findAll()

    open fun getByMail(email: String): User? = repo.findByEmail(email)

    open fun getByUsername(uname: String): User? = repo.findByUsername(uname)

    open fun delete(id: Long) { repo.deleteById(id) }

    open fun exists(user: User?): Boolean = user != null
}
