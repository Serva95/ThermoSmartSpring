package it.srv.ThermoSmartSpring.services

import it.srv.ThermoSmartSpring.dao.AuthoritiesDAO
import it.srv.ThermoSmartSpring.dao.UserDAO
import it.srv.ThermoSmartSpring.dto.UserDTO
import it.srv.ThermoSmartSpring.exception.InvalidAuthorityException
import it.srv.ThermoSmartSpring.exception.ObjectAlreadyExistException
import it.srv.ThermoSmartSpring.exception.PasswordException
import it.srv.ThermoSmartSpring.exception.UsernameAlreadyExistException
import it.srv.ThermoSmartSpring.model.Authorities
import it.srv.ThermoSmartSpring.model.User
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
open class UserService(
    private val userDAO: UserDAO,
    private val authoritiesDAO: AuthoritiesDAO,
    private val argon2PasswordEncoder: Argon2PasswordEncoder
) {

    @Throws(InvalidAuthorityException::class)
    open fun registerAdmin(admin: User) {
        val user = User()
        user.password = argon2PasswordEncoder.encode(admin.password) ?: ""
        user.email = admin.email
        user.username = admin.username
        userDAO.save(user)
        val authorities = Authorities(admin.username, "ROLE_ADMIN")
        try {
            authoritiesDAO.save(authorities)
        } catch (e: InvalidAuthorityException) {
            e.printStackTrace()
        }
    }

    @Throws(ObjectAlreadyExistException::class, UsernameAlreadyExistException::class, PasswordException::class)
    open fun registerNewUserAccount(account: UserDTO): User {
        if (userDAO.exists(userDAO.getByMail(account.email))) {
            throw ObjectAlreadyExistException(
                "È già presente un utente con questa mail: ${account.email}, prova con una diversa."
            )
        }
        if (userDAO.exists(userDAO.getByUsername(account.username))) {
            throw UsernameAlreadyExistException(
                "È già presente un nome utente come questo: ${account.username}, prova con uno diverso."
            )
        }
        val regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$"
        if (account.password.isEmpty()) {
            throw PasswordException("La password non può essere vuota")
        } else if (account.password != account.matchingPassword || account.password.length < 8) {
            throw PasswordException("Le due password sono diverse, ricontrolla e riprova.")
        } else if (!account.password.matches(Regex(regex))) {
            throw PasswordException("La password non rispetta le caratteristiche, ricontrolla e riprova.")
        }
        val user = User()
        user.password = argon2PasswordEncoder.encode(account.password) ?: ""
        user.email = account.email
        user.username = account.username
        val savedUser = userDAO.save(user)
        val authorities = Authorities(account.username, "ROLE_USER")
        try {
            authoritiesDAO.save(authorities)
        } catch (e: InvalidAuthorityException) {
            e.printStackTrace()
        }
        return savedUser
    }

    @Throws(ObjectAlreadyExistException::class, PasswordException::class)
    open fun updateAccountData(actualUser: User, newUser: UserDTO) {
        if (!argon2PasswordEncoder.matches(newUser.oldPassword, actualUser.password)) {
            throw PasswordException("La password attuale inserita è errata, controlla meglio e riprova")
        }
        if (actualUser.email.equals(newUser.email, ignoreCase = true)
            && userDAO.exists(userDAO.getByMail(newUser.email))
        ) {
            throw ObjectAlreadyExistException(
                "È già presente un utente con questa mail: ${newUser.email}, prova con una diversa."
            )
        }
        val regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$"
        if (newUser.password.isEmpty()) {
            throw PasswordException("La password non può essere vuota")
        } else if (newUser.password.matches(Regex(regex)) && newUser.password == newUser.matchingPassword) {
            actualUser.password = argon2PasswordEncoder.encode(newUser.password) ?: ""
        } else if (!newUser.password.matches(Regex(regex))) {
            throw PasswordException("La password non rispetta i caratteri richiesti")
        } else {
            throw PasswordException("La nuova password e ripeti password non corrispondono, riprova.")
        }
        actualUser.username = newUser.username
        actualUser.email = newUser.email
        userDAO.save(actualUser)
    }
}
