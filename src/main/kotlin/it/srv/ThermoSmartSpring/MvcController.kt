package it.srv.ThermoSmartSpring

import it.srv.ThermoSmartSpring.dao.InfoDAO
import it.srv.ThermoSmartSpring.dao.UserDAO
import it.srv.ThermoSmartSpring.model.Info
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RestController
class MvcController(
    private val infoDAO: InfoDAO,
    private val userDAO: UserDAO
) {

    @GetMapping("/")
    fun viewHomePage(mav: ModelAndView): ModelAndView {
        mav.setViewName("index")
        return mav
    }

    @GetMapping("/debug")
    fun debug(mav: ModelAndView): ModelAndView {
        mav.addObject("infos", infoDAO.getAll())
        mav.addObject("users", userDAO.getAll())
        mav.setViewName("debug")
        return mav
    }

    @GetMapping(path = ["/api/valveStatus"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun valveStatus(): String {
        val info = infoDAO.get("status")
        return info.value
    }
}
