package it.srv.ThermoSmartSpring.services;

import it.srv.ThermoSmartSpring.dao.OrariOnOffDAO;
import it.srv.ThermoSmartSpring.dao.RoomDAO;
import it.srv.ThermoSmartSpring.dto.OrariOnOffDTO;
import it.srv.ThermoSmartSpring.dto.OrariOnOffString;
import it.srv.ThermoSmartSpring.exception.BlankFieldsException;
import it.srv.ThermoSmartSpring.exception.InvalidFieldException;
import it.srv.ThermoSmartSpring.exception.ObjectAlreadyExistException;
import it.srv.ThermoSmartSpring.model.OrariOnOff;
import it.srv.ThermoSmartSpring.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional
public class OrariOnOffService {
    @Autowired
    OrariOnOffDAO orariOnOffDAO;

    @Autowired
    RoomDAO roomDAO;

    public void saveOrari(OrariOnOffDTO orari, int id)
            throws BlankFieldsException, InvalidFieldException, ObjectAlreadyExistException {
        Room room = roomDAO.get(id);
        ArrayList<OrariOnOff> toSave = new ArrayList<>();
        orariObjectConverter(orari, room, toSave, null);
        List<OrariOnOff> orariOnOffs = orariOnOffDAO.getByRoomId(id);
        if(orariOnOffs.iterator().hasNext()) throw new ObjectAlreadyExistException("Esistono già degli orari per questa stanza, non inserirne altri");
        orariOnOffDAO.saveAll(toSave);
    }

    public void updateOrari(OrariOnOffDTO orari, int id) throws BlankFieldsException, InvalidFieldException{
        Room room = roomDAO.get(id);
        ArrayList<OrariOnOff> toSave = new ArrayList<>();
        List<OrariOnOff> orariOnOffs = orariOnOffDAO.getByRoomId(id);
        orariObjectConverter(orari, room, toSave, orariOnOffs);
        orariOnOffDAO.saveAll(toSave);
    }

    private void orariObjectConverter(OrariOnOffDTO orari, Room room, ArrayList<OrariOnOff> toSave, List<OrariOnOff> orariOnOffs)
            throws BlankFieldsException, InvalidFieldException {
        final String msg1 = "L'orario di accensione non può essere successivo a quello di spegnimento, riprova.";
        final String msg2 = "L'orario di accensione di una fascia deve essere successivo a quello di spegnimento della fascia precedente, riprova.";
        Iterator<OrariOnOff> iter = null;
        if (orariOnOffs!= null ){
            iter = orariOnOffs.iterator();
        }
        for (OrariOnOffString e : orari.getOrariOnOffs()) {
            OrariOnOff tmp = new OrariOnOff();
            if (orariOnOffs!= null ){
                tmp.setId(iter.next().getId());
            }
            tmp.setGiorno((short) (e.getGiorno()));
            tmp.setRoom(room);
            try {
                if (e.getOrarioAccensioneA().equals(""))
                    throw new BlankFieldsException("Completa tutti i campi e riprova");
                tmp.setOrarioAccensioneA(LocalTime.parse(e.getOrarioAccensioneA()));
                if (e.getOrarioSpegnimentoA().equals(""))
                    throw new BlankFieldsException("Completa tutti i campi e riprova");
                tmp.setOrarioSpegnimentoA(LocalTime.parse(e.getOrarioSpegnimentoA()));
                if(tmp.getOrarioAccensioneA().isAfter(tmp.getOrarioSpegnimentoA()))
                    throw new InvalidFieldException(msg1);
                if (!(e.getOrarioAccensioneB().equals("") || e.getOrarioSpegnimentoB().equals(""))) {
                    tmp.setOrarioAccensioneB(LocalTime.parse(e.getOrarioAccensioneB()));
                    tmp.setOrarioSpegnimentoB(LocalTime.parse(e.getOrarioSpegnimentoB()));
                    if(tmp.getOrarioAccensioneB().isAfter(tmp.getOrarioSpegnimentoB()))
                        throw new InvalidFieldException(msg1);
                    if(tmp.getOrarioSpegnimentoA().isAfter(tmp.getOrarioAccensioneB()))
                        throw new InvalidFieldException(msg2);
                    if (!(e.getOrarioAccensioneC().equals("") || e.getOrarioSpegnimentoC().equals(""))) {
                        tmp.setOrarioAccensioneC(LocalTime.parse(e.getOrarioAccensioneC()));
                        tmp.setOrarioSpegnimentoC(LocalTime.parse(e.getOrarioSpegnimentoC()));
                        if(tmp.getOrarioAccensioneC().isAfter(tmp.getOrarioSpegnimentoC()))
                            throw new InvalidFieldException(msg1);
                        if(tmp.getOrarioSpegnimentoB().isAfter(tmp.getOrarioAccensioneC()))
                            throw new InvalidFieldException(msg2);
                    }
                }
            } catch (DateTimeParseException ex) {
                throw new InvalidFieldException("Formato non valido per gli orari, controlla e riprova");
            }
            toSave.add(tmp);
        }
    }
}
