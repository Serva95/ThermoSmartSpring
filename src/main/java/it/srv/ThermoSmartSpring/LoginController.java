package it.srv.ThermoSmartSpring;

import it.srv.ThermoSmartSpring.dto.UserDTO;
import it.srv.ThermoSmartSpring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LoginController {

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public ModelAndView viewLoginPage(ModelAndView mav) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getDetails());
        if (!authentication.getName().equals("anonymousUser")){
            mav.setViewName("redirect:/userProfile");
        } else {
            UserDTO user = new UserDTO();
            mav.setViewName("login");
            mav.addObject("user", user);
        }
        return mav;
    }

}