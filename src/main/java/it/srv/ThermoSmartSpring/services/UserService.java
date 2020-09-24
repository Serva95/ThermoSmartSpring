package it.srv.ThermoSmartSpring.services;

import it.srv.ThermoSmartSpring.dao.AuthoritiesDAO;
import it.srv.ThermoSmartSpring.dao.UserDAO;
import it.srv.ThermoSmartSpring.dto.UserDTO;
import it.srv.ThermoSmartSpring.exception.InvalidAuthorityException;
import it.srv.ThermoSmartSpring.exception.PasswordException;
import it.srv.ThermoSmartSpring.exception.UserAlreadyExistException;
import it.srv.ThermoSmartSpring.exception.UsernameAlreadyExistException;
import it.srv.ThermoSmartSpring.model.Authorities;
import it.srv.ThermoSmartSpring.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    @Autowired
    public SCryptPasswordEncoder sCryptPasswordEncoder() {
        return new SCryptPasswordEncoder();
    }

    @Autowired
    public Argon2PasswordEncoder argon2PasswordEncoder() { return new Argon2PasswordEncoder(16,64,8,1<<13,16);}

    @Autowired
    UserDAO userDAO;

    @Autowired
    AuthoritiesDAO authoritiesDAO;

    public User registerNewUserAccount(final UserDTO account) throws
            UserAlreadyExistException,
            UsernameAlreadyExistException,
            PasswordException {
        if (userDAO.exists(userDAO.getByMail(account.getEmail()))) {
            throw new UserAlreadyExistException(
                    "È già presente un utente con questa mail: " + account.getEmail() + ", prova con una diversa.");
        }
        if (userDAO.exists(userDAO.getByUsername(account.getUsername()))) {
            throw new UsernameAlreadyExistException(
                    "È già presente un nome utente come questo: " + account.getUsername() + ", prova con uno diverso.");
        }
        if (!account.getPassword().equals(account.getMatchingPassword()) || account.getPassword().length()<8){
            throw new PasswordException("La password non rispetta le caratteristiche o le due password sono diverse, ricontrolla e riprova.");
        }
        User user = new User();
        user.setPassword(argon2PasswordEncoder().encode(account.getPassword()));
        user.setEmail(account.getEmail());
        user.setUsername(account.getUsername());
        user = userDAO.save(user);
        Authorities authorities = new Authorities(account.getUsername(), "ROLE_USER");
        try {
            authoritiesDAO.save(authorities);
        } catch (InvalidAuthorityException e) {
            e.printStackTrace();
        }
        return user;
    }

}
