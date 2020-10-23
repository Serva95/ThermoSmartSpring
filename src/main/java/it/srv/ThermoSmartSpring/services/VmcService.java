package it.srv.ThermoSmartSpring.services;

import it.srv.ThermoSmartSpring.dao.VmcDAO;
import it.srv.ThermoSmartSpring.dto.VmcStringDTO;
import it.srv.ThermoSmartSpring.exception.BlankFieldsException;
import it.srv.ThermoSmartSpring.exception.InvalidFieldException;
import it.srv.ThermoSmartSpring.exception.ObjectAlreadyExistException;
import it.srv.ThermoSmartSpring.model.Vmc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

@Service
@Transactional
public class VmcService {
    @Autowired
    VmcDAO vmcDAO;

    private final String idError = "L'id non può essere vuoto";
    private final String idLengthError = "Id non valido, deve essere di 32 caratteri alfanumerici";

    public void saveNew (Vmc vmc) throws BlankFieldsException, InvalidFieldException {
        if(vmc.getId().equals(""))
            throw new BlankFieldsException(idError);
        if(!(vmc.getId().length()==32 && vmc.getId().matches("[a-zA-Z0-9]*")))
            throw new InvalidFieldException(idLengthError);
        vmc.setImpostazioneFunzione(false);
        vmc.setStatoAttuale(false);
        vmcDAO.save(vmc);
    }

    public void saveEditedVmc (VmcStringDTO vmc, String id) throws BlankFieldsException, InvalidFieldException, ObjectAlreadyExistException {
        Vmc old = vmcDAO.get(id);
        Vmc newVmc = new Vmc();
        if(vmc.getId().equals(""))
            throw new BlankFieldsException(idError);
        if(!(vmc.getId().length()==32 && vmc.getId().matches("[a-zA-Z0-9]*")))
            throw new InvalidFieldException(idLengthError);
        if(!vmc.getId().equals(id) && vmcDAO.exists(vmc.getId()))
            throw new ObjectAlreadyExistException("Esiste già una vmc con questo id salvata, prova con uno diverso");
        newVmc.setId(vmc.getId());
        newVmc.setStatoAttuale(old.getStatoAttuale());
        newVmc.setImpostazioneFunzione(old.getImpostazioneFunzione());
        try {
            if(!vmc.getProgrammedOnTime().equals(""))
                newVmc.setProgrammedOnTime(LocalTime.parse(vmc.getProgrammedOnTime()));
            else
                newVmc.setProgrammedOnTime(null);
            if(!vmc.getProgrammedOffTime().equals(""))
                newVmc.setProgrammedOffTime(LocalTime.parse(vmc.getProgrammedOffTime()));
            else
                newVmc.setProgrammedOffTime(null);
        } catch (DateTimeParseException ex) {
            throw new InvalidFieldException("Formato non valido per gli orari, controlla e riprova");
        }
        vmcDAO.update(newVmc, id);
    }

    public void updateStatus(Vmc vmc){
        vmc.setImpostazioneFunzione(!vmc.getImpostazioneFunzione());
        vmcDAO.save(vmc);
    }

    public void delete(Vmc vmc){
        vmcDAO.delete(vmc.getId());
    }
}
