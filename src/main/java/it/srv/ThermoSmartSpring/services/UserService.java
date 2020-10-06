package it.srv.ThermoSmartSpring.services;

import it.srv.ThermoSmartSpring.dao.AuthoritiesDAO;
import it.srv.ThermoSmartSpring.dao.UserDAO;
import it.srv.ThermoSmartSpring.dto.UserDTO;
import it.srv.ThermoSmartSpring.exception.InvalidAuthorityException;
import it.srv.ThermoSmartSpring.exception.ObjectAlreadyExistException;
import it.srv.ThermoSmartSpring.exception.PasswordException;
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
            ObjectAlreadyExistException,
            UsernameAlreadyExistException,
            PasswordException {
        if (userDAO.exists(userDAO.getByMail(account.getEmail()))) {
            throw new ObjectAlreadyExistException(
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

    public User updateAccountData(User actualUser, UserDTO newUser) throws
            ObjectAlreadyExistException,
            PasswordException {
        if(!argon2PasswordEncoder().matches(newUser.getOldPassword(), actualUser.getPassword())){
            throw new PasswordException("La password attuale inserita è errata, controlla meglio e riprova");
        }
        if(actualUser.getEmail().compareToIgnoreCase(newUser.getEmail()) != 0
        && userDAO.exists(userDAO.getByMail(newUser.getEmail()))){
            throw new ObjectAlreadyExistException(
                    "È già presente un utente con questa mail: " + newUser.getEmail() + ", prova con una diversa.");
        }
        if(newUser.getPassword() != null
                && newUser.getPassword().length() >= 8
                && newUser.getPassword().compareTo(newUser.getMatchingPassword()) == 0){
            actualUser.setPassword(argon2PasswordEncoder().encode(newUser.getPassword()));
        } else if(newUser.getPassword() != null && newUser.getPassword().equals("")){
        } else {
            throw new PasswordException("La nuova password e ripeti password non corrispondono, oppure non rispetta la lunghezza minima, riprova.");
        }
        actualUser.setUsername(newUser.getUsername());
        actualUser.setEmail(newUser.getEmail());
        return userDAO.save(actualUser);
    }

}
