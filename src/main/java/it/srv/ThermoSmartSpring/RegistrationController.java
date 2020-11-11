package it.srv.ThermoSmartSpring;

import it.srv.ThermoSmartSpring.dto.UserDTO;
import it.srv.ThermoSmartSpring.exception.ObjectAlreadyExistException;
import it.srv.ThermoSmartSpring.exception.PasswordException;
import it.srv.ThermoSmartSpring.exception.UsernameAlreadyExistException;
import it.srv.ThermoSmartSpring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class RegistrationController {

    @Autowired
    UserService userService;

    public RegistrationController() {
        super();
    }

    @GetMapping("/register")
    public ModelAndView viewHomePage(ModelAndView mav) {
        UserDTO user = new UserDTO();
        mav.setViewName("register");
        mav.addObject("user", user);
        return mav;
    }

    @PostMapping("/register")
    public ModelAndView registerUserAccount(@ModelAttribute("user") final UserDTO user) {
        try {
            userService.registerNewUserAccount(user);
        } catch (final ObjectAlreadyExistException | PasswordException | UsernameAlreadyExistException ex) {
            ModelAndView mav = new ModelAndView("register", "user", user);
            mav.addObject("message", ex.getMessage());
            return mav;
        } catch (final RuntimeException ex) {
            ModelAndView mav = new ModelAndView("error", "user", user);
            mav.addObject("message", ex.getMessage());
            return mav;
        }
        ModelAndView mav = new ModelAndView("success", "user", user);
        mav.addObject("message", "Registrazione effettuata. Ora puoi fare login.");
        return mav;
    }
}
