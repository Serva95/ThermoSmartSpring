package it.srv.ThermoSmartSpring.dao;

import it.srv.ThermoSmartSpring.model.User;
import it.srv.ThermoSmartSpring.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDAO {
    @Autowired
    private UserRepository repo;

    public User save(User user) { return repo.save(user); }

    public User getByMail(String email) { return repo.findByEmail(email); }

    public void delete(long id) { repo.deleteById(id); }

    public boolean exists(User user){ return user != null; }
}
