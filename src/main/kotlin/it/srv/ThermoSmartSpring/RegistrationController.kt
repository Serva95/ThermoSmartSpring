package it.srv.ThermoSmartSpring

import it.srv.ThermoSmartSpring.dto.UserDTO
import it.srv.ThermoSmartSpring.exception.ObjectAlreadyExistException
import it.srv.ThermoSmartSpring.exception.PasswordException
import it.srv.ThermoSmartSpring.exception.UsernameAlreadyExistException
import it.srv.ThermoSmartSpring.services.UserService
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@RestController
@RequestMapping("/register")
class RegistrationController(
    private val userService: UserService
) {

    @GetMapping("")
    fun viewHomePage(mav: ModelAndView): ModelAndView {
        mav.setViewName("register")
        mav.addObject("user", UserDTO())
        return mav
    }

    @PostMapping("")
    fun registerUserAccount(@ModelAttribute("user") user: UserDTO): ModelAndView {
        return try {
            userService.registerNewUserAccount(user)
            ModelAndView("success", "user", user).apply {
                addObject("message", "Registrazione effettuata. Ora puoi fare login.")
            }
        } catch (ex: Exception) {
            when (ex) {
                is ObjectAlreadyExistException, is PasswordException, is UsernameAlreadyExistException -> {
                    ModelAndView("register", "user", user).apply {
                        addObject("message", ex.message)
                    }
                }
                else -> ModelAndView("error", "user", user).apply {
                    addObject("message", ex.message)
                }
            }
        }
    }
}
