package it.srv.ThermoSmartSpring

import it.srv.ThermoSmartSpring.dao.VmcDAO
import it.srv.ThermoSmartSpring.dto.VmcStringDTO
import it.srv.ThermoSmartSpring.exception.BlankFieldsException
import it.srv.ThermoSmartSpring.exception.InvalidFieldException
import it.srv.ThermoSmartSpring.exception.ObjectAlreadyExistException
import it.srv.ThermoSmartSpring.model.Vmc
import it.srv.ThermoSmartSpring.services.VmcService
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@RestController
class VmcController(
    private val vmcDAO: VmcDAO,
    private val vmcService: VmcService
) {

    @GetMapping("/vmc")
    fun vmc(mav: ModelAndView): ModelAndView {
        val vmc = vmcDAO.findFirst()
        mav.addObject("vmc", vmc)
        mav.setViewName("viewVMC")
        return mav
    }

    @GetMapping("/vmc/new")
    fun newVmc(mav: ModelAndView): ModelAndView {
        mav.addObject("vmc", Vmc(""))
        mav.setViewName("newVMC")
        return mav
    }

    @GetMapping("/vmc/{id}/edit")
    fun editVMC(mav: ModelAndView, @PathVariable id: String): ModelAndView {
        val vmc = vmcDAO.get(id)
        val stringVMC = VmcStringDTO().apply {
            this.id = vmc.id
            this.statoAttuale = vmc.statoAttuale ?: false
            this.impostazioneFunzione = vmc.impostazioneFunzione ?: false
            this.programmedOnTime = vmc.programmedOnTime?.toString()
            this.programmedOffTime = vmc.programmedOffTime?.toString()
        }
        mav.addObject("vmc", stringVMC)
        mav.setViewName("editVMC")
        return mav
    }

    @PostMapping("/vmc/new")
    fun createVMC(mav: ModelAndView, @ModelAttribute("vmc") vmc: Vmc): ModelAndView {
        try {
            vmcService.saveNew(vmc)
            mav.addObject("message", "VMC creata con successo.")
            mav.setViewName("redirect:/vmc")
            return mav
        } catch (e: Exception) {
            when (e) {
                is BlankFieldsException, is InvalidFieldException -> {
                    mav.addObject("vmc", vmc)
                    mav.addObject("message", e.message)
                    mav.setViewName("newVMC")
                    return mav
                }
                else -> throw e
            }
        }
    }

    @PutMapping("/vmc/{id}")
    fun updateVMC(
            mav: ModelAndView,
            @PathVariable id: String,
            @ModelAttribute("vmc") vmc: VmcStringDTO): ModelAndView {
        if (!vmcDAO.exists(id)) {
            mav.addObject("message", "VMC non trovata.")
            mav.setViewName("redirect:/vmc")
            return mav
        }
        try {
            vmcService.saveEditedVmc(vmc, id)
            mav.addObject("message", "VMC modificata con successo.")
            mav.setViewName("redirect:/vmc")
            return mav
        } catch (e: Exception) {
            when (e) {
                is BlankFieldsException, is InvalidFieldException, is ObjectAlreadyExistException -> {
                    val dbVmc = vmcDAO.get(id)
                    mav.addObject("vmc", dbVmc)
                    mav.addObject("message", e.message)
                    mav.setViewName("editVMC")
                    return mav
                }
                else -> throw e
            }
        }
    }

    @PutMapping("/secure-api/vmc/{id}/changestate")
    @ResponseBody
    fun changeVMCState(@PathVariable id: String): String {
        val vmc = vmcDAO.get(id) ?: return "VMC non trovata."
        return vmcService.updateStatus(vmc).toString()
    }

    @DeleteMapping("/vmc/{id}")
    fun deleteVMC(mav: ModelAndView, @PathVariable id: String): ModelAndView {
        val vmc = vmcDAO.get(id)
        if (vmc == null) {
            mav.addObject("message", "VMC non trovata.")
            mav.setViewName("redirect:/vmc")
            return mav
        }
        vmcService.delete(vmc)
        mav.addObject("message", "VMC eliminata.")
        mav.setViewName("redirect:/vmc")
        return mav
    }
}
