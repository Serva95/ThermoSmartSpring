package it.srv.ThermoSmartSpring

import it.srv.ThermoSmartSpring.dao.AuthoritiesDAO
import it.srv.ThermoSmartSpring.dao.UserDAO
import it.srv.ThermoSmartSpring.dto.UserDTO
import it.srv.ThermoSmartSpring.exception.ObjectAlreadyExistException
import it.srv.ThermoSmartSpring.exception.PasswordException
import it.srv.ThermoSmartSpring.model.Authorities
import it.srv.ThermoSmartSpring.model.User
import it.srv.ThermoSmartSpring.services.UserService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@RestController
@RequestMapping("/userProfile")
class UserController(
    private val userDAO: UserDAO,
    private val authoritiesDAO: AuthoritiesDAO,
    private val userService: UserService
) {

    private fun getAuthenticatedUsername(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication?.name ?: throw IllegalStateException("Not authenticated")
    }

    @GetMapping("")
    fun userProfile(mav: ModelAndView): ModelAndView {
        val username = getAuthenticatedUsername()
        val user = userDAO.getByUsername(username) ?: throw IllegalStateException("User not found")
        val authList = authoritiesDAO.getByUsername(username) ?: emptyList()
        val authorities = authList.toTypedArray()
        mav.setViewName("userProfile")
        mav.addObject("user", user)
        mav.addObject("authorities", authorities)
        return mav
    }

    @GetMapping("/{id}")
    fun userProfileEdit(@PathVariable id: Long): ModelAndView {
        val username = getAuthenticatedUsername()
        val user = userDAO.getByUsername(username) ?: throw IllegalStateException("User not found")
        val mav = ModelAndView("editUser")
        val userDTO = UserDTO(user.username, "", "", user.email, "")
        mav.addObject("userDTO", userDTO)
        mav.addObject("userId", user.id)
        return mav
    }

    @PutMapping("/{id}")
    fun userProfileEditSave(
            mav: ModelAndView,
            @PathVariable id: Long,
            @ModelAttribute("userDTO") user: UserDTO,
            @RequestParam("matchingPassword") matchingPassword: String
    ): ModelAndView {
        val username = getAuthenticatedUsername()
        val actualUser = userDAO.getByUsername(username) ?: throw IllegalStateException("User not found")
        user.username = actualUser.username
        
        if (actualUser.id != id) {
            val errorMav = ModelAndView("editUser")
            errorMav.addObject("userDTO", user)
            errorMav.addObject("userId", actualUser.id)
            errorMav.addObject("message", "Ehi che fai ? Puoi modificare solo il tuo profilo!")
            return errorMav
        }
        
        return try {
            userService.updateAccountData(actualUser, user)
            mav.addObject("message", "Modifiche salvate.")
            mav.setViewName("redirect:/userProfile")
            mav
        } catch (e: Exception) {
            when (e) {
                is ObjectAlreadyExistException, is PasswordException -> {
                    val errorMav = ModelAndView("editUser")
                    errorMav.addObject("userDTO", user)
                    errorMav.addObject("userId", actualUser.id)
                    errorMav.addObject("message", e.message)
                    errorMav
                }
                else -> throw e
            }
        }
    }
}
