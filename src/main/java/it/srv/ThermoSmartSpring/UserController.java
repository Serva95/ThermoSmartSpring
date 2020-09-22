package it.srv.ThermoSmartSpring;

import it.srv.ThermoSmartSpring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserService userService;

}
