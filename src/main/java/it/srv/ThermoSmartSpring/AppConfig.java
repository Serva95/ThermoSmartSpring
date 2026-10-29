package it.srv.ThermoSmartSpring;

import it.srv.ThermoSmartSpring.dao.AuthoritiesDAO;
import it.srv.ThermoSmartSpring.dao.UserDAO;
import it.srv.ThermoSmartSpring.model.Authorities;
import it.srv.ThermoSmartSpring.model.User;
import it.srv.ThermoSmartSpring.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class AppConfig {

    private final UserDAO userDAO;
    private final AuthoritiesDAO authoritiesDAO;
    private final UserService userService;

    public AppConfig(UserDAO userDAO, AuthoritiesDAO authoritiesDAO, UserService userService) {
        this.userDAO = userDAO;
        this.authoritiesDAO = authoritiesDAO;
        this.userService = userService;
    }

    @Bean
    public CommandLineRunner adminRunner() {
        return args -> {
            User admin = userDAO.getByUsername("thermo-admin");
            ArrayList<Authorities> admins = authoritiesDAO.getByAuthority("ROLE_ADMIN");
            if (admin == null && admins.isEmpty()) {
                admin = new User();
                admin.setUsername("thermo-admin");
                admin.setEmail("admin@thermo.app");
                admin.setPassword("thermo-admin");
                userService.registerAdmin(admin);
            }
        };
    }
}
