package it.srv.ThermoSmartSpring

import it.srv.ThermoSmartSpring.dao.OrariOnOffDAO
import it.srv.ThermoSmartSpring.dto.OrariOnOffDTO
import it.srv.ThermoSmartSpring.dto.OrariOnOffString
import it.srv.ThermoSmartSpring.exception.BlankFieldsException
import it.srv.ThermoSmartSpring.exception.InvalidFieldException
import it.srv.ThermoSmartSpring.exception.ObjectAlreadyExistException
import it.srv.ThermoSmartSpring.model.OrariOnOff
import it.srv.ThermoSmartSpring.services.OrariOnOffService
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

@RestController
class OrariOnOffController(
    private val orariOnOffDAO: OrariOnOffDAO,
    private val orariOnOffService: OrariOnOffService
) {

    @GetMapping("/rooms/{id}/orarionoff")
    fun orariOnOffs(mav: ModelAndView, @PathVariable id: Long): ModelAndView {
        val orariOnOffs = orariOnOffDAO.getByRoomId(id)
        getDaysOfWeek(mav)
        mav.addObject("orarionoffs", orariOnOffs)
        mav.setViewName("viewOrariOnOff")
        return mav
    }

    @GetMapping("/rooms/{id}/orarionoff/new")
    fun newOrariOnOffs(mav: ModelAndView, @PathVariable id: Long): ModelAndView {
        val orariOnOffDTO = OrariOnOffDTO()
        for (i in 0 until 7) {
            orariOnOffDTO.add(OrariOnOffString(), i.toShort())
        }
        getDaysOfWeek(mav)
        mav.addObject("orarionoffDTO", orariOnOffDTO)
        mav.setViewName("newOrariOnOff")
        return mav
    }

    @PostMapping("/rooms/{id}/orarionoff")
    fun createOrariOnOff(
            mav: ModelAndView,
            @PathVariable id: Long,
            @ModelAttribute("orarionoffDTO") orariOnOffDTO: OrariOnOffDTO): ModelAndView {
        try {
            orariOnOffService.saveOrari(orariOnOffDTO, id)
            mav.addObject("message", "Orari salvati con successo.")
            mav.setViewName("redirect:/rooms/$id/orarionoff")
            return mav
        } catch (e: Exception) {
            when (e) {
                is BlankFieldsException, is InvalidFieldException, is ObjectAlreadyExistException -> {
                    mav.addObject("message", e.message)
                    mav.addObject("orarionoffDTO", orariOnOffDTO)
                    mav.setViewName("newOrariOnOff")
                    getDaysOfWeek(mav)
                    return mav
                }
                else -> throw e
            }
        }
    }

    @GetMapping("/rooms/{id}/orarionoff/edit")
    fun editOrariOnOff(mav: ModelAndView, @PathVariable id: Long): ModelAndView {
        val orariOnOffs = orariOnOffDAO.getByRoomId(id)
        val orariOnOffDTO = OrariOnOffDTO()
        orariOnOffDTO.orariToStringDTO(orariOnOffs)
        mav.addObject("orarionoffDTO", orariOnOffDTO)
        getDaysOfWeek(mav)
        mav.setViewName("editOrariOnOff")
        return mav
    }

    @PutMapping("/rooms/{id}/orarionoff/edit")
    fun updateOrariOnOff(
            mav: ModelAndView,
            @PathVariable id: Long,
            @ModelAttribute("orarionoffDTO") orariOnOffDTO: OrariOnOffDTO): ModelAndView {
        try {
            orariOnOffService.updateOrari(orariOnOffDTO, id)
            mav.addObject("message", "Orari modificati con successo.")
            mav.setViewName("redirect:/rooms/$id/orarionoff")
            return mav
        } catch (e: Exception) {
            when (e) {
                is BlankFieldsException, is InvalidFieldException -> {
                    mav.addObject("message", e.message)
                    mav.addObject("orarionoffDTO", orariOnOffDTO)
                    mav.setViewName("editOrariOnOff")
                    getDaysOfWeek(mav)
                    return mav
                }
                else -> throw e
            }
        }
    }

    private fun getDaysOfWeek(mav: ModelAndView) {
        val days = mutableListOf<String>()
        for (i in 0 until 7) {
            days.add(DayOfWeek.of(i + 1).getDisplayName(TextStyle.FULL, Locale.ITALY))
        }
        mav.addObject("days", days)
    }
}
