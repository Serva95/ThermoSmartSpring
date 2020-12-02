package it.srv.ThermoSmartSpring;

import it.srv.ThermoSmartSpring.dao.VmcDAO;
import it.srv.ThermoSmartSpring.dto.VmcStringDTO;
import it.srv.ThermoSmartSpring.exception.BlankFieldsException;
import it.srv.ThermoSmartSpring.exception.InvalidFieldException;
import it.srv.ThermoSmartSpring.exception.ObjectAlreadyExistException;
import it.srv.ThermoSmartSpring.model.Vmc;
import it.srv.ThermoSmartSpring.services.VmcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class VmcController {
    @Autowired
    VmcDAO vmcDAO;

    @Autowired
    VmcService vmcService;

    @GetMapping("/vmc")
    public ModelAndView vmc(ModelAndView mav) {
        Vmc vmc = vmcDAO.findFirst();
        mav.addObject("vmc", vmc);
        mav.setViewName("viewVMC");
        return mav;
    }

    @GetMapping("/vmc/new")
    public ModelAndView newVmc(ModelAndView mav) {
        Vmc vmc = new Vmc();
        mav.addObject("vmc", vmc);
        mav.setViewName("newVMC");
        return mav;
    }

    @GetMapping("/vmc/{id}/edit")
    public ModelAndView editVMC(ModelAndView mav, @PathVariable String id) {
        Vmc vmc = vmcDAO.get(id);
        VmcStringDTO stringVMC = new VmcStringDTO();
        stringVMC.setId(vmc.getId());
        stringVMC.setImpostazioneFunzione(vmc.getImpostazioneFunzione());
        stringVMC.setStatoAttuale(vmc.getStatoAttuale());
        stringVMC.setProgrammedOnTime((vmc.getProgrammedOnTime() != null) ? vmc.getProgrammedOnTime().toString() : null);
        stringVMC.setProgrammedOffTime((vmc.getProgrammedOffTime() != null) ? vmc.getProgrammedOffTime().toString() : null);
        mav.addObject("vmc", stringVMC);
        mav.setViewName("editVMC");
        return mav;
    }

    @PostMapping("/vmc/new")
    public ModelAndView createVMC(ModelAndView mav, @ModelAttribute("vmc") final Vmc vmc) {
        try {
            vmcService.saveNew(vmc);
        } catch (BlankFieldsException | InvalidFieldException e) {
            mav.addObject("vmc", vmc);
            mav.addObject("message", e.getMessage());
            mav.setViewName("newVMC");
            return mav;
        }
        mav.addObject("message", "VMC creata con successo.");
        mav.setViewName("redirect:/vmc");
        return mav;
    }

    @PutMapping("/vmc/{id}")
    public ModelAndView updateVMC(
            ModelAndView mav,
            @PathVariable String id,
            @ModelAttribute("vmc") final VmcStringDTO vmc) {
        if(!vmcDAO.exists(id)){
            mav.addObject("message", "VMC non trovata.");
            mav.setViewName("redirect:/vmc");
            return mav;
        }
        try {
            vmcService.saveEditedVmc(vmc, id);
        } catch (BlankFieldsException | InvalidFieldException | ObjectAlreadyExistException e) {
            Vmc dbVmc = vmcDAO.get(id);
            mav.addObject("vmc", dbVmc);
            mav.addObject("message", e.getMessage());
            mav.setViewName("editVMC");
            return mav;
        }
        mav.addObject("message", "VMC modificata con successo.");
        mav.setViewName("redirect:/vmc");
        return mav;
    }

    @PutMapping("/secure-api/vmc/{id}/changestate")
    public String changeVMCState(@PathVariable String id) {
        Vmc vmc = vmcDAO.get(id);
        if(vmc == null)
            return "VMC non trovata.";
        return String.valueOf(vmcService.updateStatus(vmc));
    }

    @DeleteMapping("/vmc/{id}")
    public ModelAndView deleteVMC(ModelAndView mav, @PathVariable String id) {
        Vmc vmc = vmcDAO.get(id);
        if(vmc == null){
            mav.addObject("message", "VMC non trovata.");
            mav.setViewName("redirect:/vmc");
            return mav;
        }
        vmcService.delete(vmc);
        mav.addObject("message", "VMC eliminata.");
        mav.setViewName("redirect:/vmc");
        return mav;
    }
}
