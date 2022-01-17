package it.srv.ThermoSmartSpring;

import it.srv.ThermoSmartSpring.dao.AuthoritiesDAO;
import it.srv.ThermoSmartSpring.dao.UserDAO;
import it.srv.ThermoSmartSpring.dto.UserDTO;
import it.srv.ThermoSmartSpring.exception.ObjectAlreadyExistException;
import it.srv.ThermoSmartSpring.exception.PasswordException;
import it.srv.ThermoSmartSpring.model.Authorities;
import it.srv.ThermoSmartSpring.model.User;
import it.srv.ThermoSmartSpring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@RestController
@RequestMapping("/userProfile")
public class UserController {

    @Autowired
    UserDAO userDAO;

    @Autowired
    AuthoritiesDAO authoritiesDAO;

    @Autowired
    UserService userService;

    @GetMapping("")
    public ModelAndView userProfile(ModelAndView mav) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userDAO.getByUsername(authentication.getName());
        ArrayList<Authorities> authList = authoritiesDAO.getByUsername(authentication.getName());
        Authorities[] authorities = new Authorities[authList.size()];
        authorities = authList.toArray(authorities);
        mav.setViewName("userProfile");
        mav.addObject("user", user);
        mav.addObject("authorities", authorities);
        return mav;
    }

    @GetMapping("/{id}")
    public ModelAndView userProfileEdit(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userDAO.getByUsername(authentication.getName());
        ModelAndView mav = new ModelAndView("editUser");
        UserDTO userDTO = new UserDTO(user.getUsername(), "", "", user.getEmail(), "");
        mav.addObject("userDTO", userDTO);
        mav.addObject("userId", user.getId());
        return mav;
    }

    @PutMapping("/{id}")
    public ModelAndView userProfileEditSave (
            ModelAndView mav,
            @PathVariable int id,
            @ModelAttribute("userDTO") final UserDTO user,
            @RequestParam("matchingPassword") final String matchingPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User actualUser = userDAO.getByUsername(authentication.getName());
        user.setUsername(actualUser.getUsername());
        if (actualUser.getId() != id){
            mav = new ModelAndView("editUser");
            mav.addObject("userDTO", user);
            mav.addObject("userId", actualUser.getId());
            mav.addObject("message", "Ehi che fai ? Puoi modificare solo il tuo profilo!");
            return mav;
        }
        try {
            userService.updateAccountData(actualUser, user);
        } catch (ObjectAlreadyExistException | PasswordException e) {
            mav = new ModelAndView("editUser");
            mav.addObject("userDTO", user);
            mav.addObject("userId", actualUser.getId());
            mav.addObject("message", e.getMessage());
            return mav;
        }
        mav.addObject("message", "Modifiche salvate.");
        mav.setViewName("redirect:/userProfile");
        return mav;
    }
}
