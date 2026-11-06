package it.srv.ThermoSmartSpring

import it.srv.ThermoSmartSpring.dao.AuthoritiesDAO
import it.srv.ThermoSmartSpring.dao.UserDAO
import it.srv.ThermoSmartSpring.model.User
import it.srv.ThermoSmartSpring.services.UserService
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class AppConfig(
    private val userDAO: UserDAO,
    private val authoritiesDAO: AuthoritiesDAO,
    private val userService: UserService
) {

    @Bean
    open fun adminRunner(): CommandLineRunner = CommandLineRunner { args ->
        val admin = userDAO.getByUsername("thermo-admin")
        val admins = authoritiesDAO.getByAuthority("ROLE_ADMIN")
        if (admin == null && admins.isEmpty()) {
            val newAdmin = User()
            newAdmin.username = "thermo-admin"
            newAdmin.email = "admin@thermo.app"
            newAdmin.password = "thermo-admin"
            userService.registerAdmin(newAdmin)
        }
    }
}
