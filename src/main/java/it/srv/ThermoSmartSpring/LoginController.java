package it.srv.ThermoSmartSpring;

import it.srv.ThermoSmartSpring.dto.UserDTO;
import it.srv.ThermoSmartSpring.model.User;
import it.srv.ThermoSmartSpring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
public class LoginController {

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public ModelAndView viewLoginPage(ModelAndView mav) {
        UserDTO user = new UserDTO();
        mav.setViewName("login");
        mav.addObject("user", user);
        return mav;
    }

    /*@PostMapping("/login")
    public ModelAndView postLoginPage(@ModelAttribute("user") final UserDTO user, ModelAndView mav) {
        User registered;
        try {
            registered = userService.findRegisteredUser(user);
        } catch (UserNotRegisteredException | PasswordException e) {
            System.out.println("exc");
            mav = new ModelAndView("login", "user", user);
            mav.addObject("message", e.getMessage());
            return mav;
        }
        System.out.println("passed");
        authWithPassword(registered);
        mav.setViewName("login");
        mav.addObject("user", user);
        return mav;
    }*/

    private void authWithPassword(User user) {

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getEmail().equals("serva95@icloud.com")) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("USER"));
        }
        System.out.println("auth");
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}