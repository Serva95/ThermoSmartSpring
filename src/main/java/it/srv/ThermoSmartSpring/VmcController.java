package it.srv.ThermoSmartSpring;

import it.srv.ThermoSmartSpring.dao.VmcDAO;
import it.srv.ThermoSmartSpring.model.Vmc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class VmcController {
    @Autowired
    VmcDAO vmcDAO;

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
    public ModelAndView viewVMC(ModelAndView mav, @PathVariable int id) {
        return mav;
    }

    @PostMapping("/vmc/new")
    public ModelAndView createVMC(ModelAndView mav, @ModelAttribute("vmc") final Vmc vmc) {
        //da fare col service aggiungendo i false !!!!!!!!!!!!!
        vmcDAO.save(vmc);
        mav.addObject("message", "VMC creata con successo.");
        mav.setViewName("redirect:/vmc");
        return mav;
    }

    @PutMapping("/vmc/{id}")
    public ModelAndView updateVMC(ModelAndView mav, @PathVariable int id) {
        return mav;
    }

    @DeleteMapping("/vmc/{id}")
    public ModelAndView deleteVMC(ModelAndView mav, @PathVariable int id) {
        return mav;
    }
}
