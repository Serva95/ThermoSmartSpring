package it.srv.ThermoSmartSpring

import it.srv.ThermoSmartSpring.dto.UserDTO
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RestController
class LoginController {

    @GetMapping("/login")
    fun viewLoginPage(mav: ModelAndView): ModelAndView {
        val authentication = SecurityContextHolder.getContext().authentication
        val name = authentication?.name ?: "anonymousUser"
        if (name != "anonymousUser") {
            mav.viewName = "redirect:/userProfile"
        } else {
            mav.viewName = "login"
            mav.addObject("user", UserDTO())
        }
        return mav
    }
}
