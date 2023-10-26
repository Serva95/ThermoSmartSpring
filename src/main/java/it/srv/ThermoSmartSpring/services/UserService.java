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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    @Autowired
    public Argon2PasswordEncoder argon2PasswordEncoder() { return new Argon2PasswordEncoder(16,64,8,1<<13,16);}

    @Autowired
    UserDAO userDAO;

    @Autowired
    AuthoritiesDAO authoritiesDAO;

    public void registerAdmin(final User admin) {
        User user = new User();
        user.setPassword(argon2PasswordEncoder().encode(admin.getPassword()));
        user.setEmail(admin.getEmail());
        user.setUsername(admin.getUsername());
        userDAO.save(user);
        Authorities authorities = new Authorities(admin.getUsername(), "ROLE_ADMIN");
        try {
            authoritiesDAO.save(authorities);
        } catch (InvalidAuthorityException e) {
            e.printStackTrace();
        }
    }

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
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
        if (account.getPassword() != null) {
            if (!account.getPassword().equals(account.getMatchingPassword()) || account.getPassword().length() < 8) {
                throw new PasswordException("Le due password sono diverse, ricontrolla e riprova.");
            } else if (!account.getPassword().matches(regex)){
                throw new PasswordException("La password non rispetta le caratteristiche, ricontrolla e riprova.");
            }
        } else {
            throw new PasswordException("La password non può essere vuota");
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

    public void updateAccountData(User actualUser, UserDTO newUser) throws
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
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
        if(newUser.getPassword() != null) {
            if (newUser.getPassword().matches(regex) && newUser.getPassword().compareTo(newUser.getMatchingPassword()) == 0) {
                actualUser.setPassword(argon2PasswordEncoder().encode(newUser.getPassword()));
            } else if (!newUser.getPassword().matches(regex)) {
                throw new PasswordException("La password non rispetta i caratteri richiesti");
            } else {
                throw new PasswordException("La nuova password e ripeti password non corrispondono, riprova.");
            }
        } else {
            throw new PasswordException("La password non può essere vuota");
        }
        actualUser.setUsername(newUser.getUsername());
        actualUser.setEmail(newUser.getEmail());
        userDAO.save(actualUser);
    }

}
