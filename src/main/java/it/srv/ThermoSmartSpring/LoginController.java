package it.srv.ThermoSmartSpring;

import it.srv.ThermoSmartSpring.dto.UserDTO;
import it.srv.ThermoSmartSpring.model.User;
import it.srv.ThermoSmartSpring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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

    @PostMapping("/login")
    public ModelAndView postLoginPage(@ModelAttribute("user") final UserDTO user, ModelAndView mav) {
        mav.setViewName("login");
        mav.addObject("user", user);

        return mav;
    }

    public void authWithoutPassword(User user) {

        /*List<Privilege> privileges = user.getRoles()
                .stream()
                .map(Role::getPrivileges)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
*/
        /*List<GrantedAuthority> authorities = privileges.stream()
                .map(p -> new SimpleGrantedAuthority(p.getName()))
                .collect(Collectors.toList());

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);*/
    }

}