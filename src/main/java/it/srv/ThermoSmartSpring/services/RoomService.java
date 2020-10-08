package it.srv.ThermoSmartSpring.services;

import it.srv.ThermoSmartSpring.dao.RoomDAO;
import it.srv.ThermoSmartSpring.exception.BlankFieldsException;
import it.srv.ThermoSmartSpring.exception.InvalidFieldException;
import it.srv.ThermoSmartSpring.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class RoomService {
    @Autowired
    RoomDAO roomDAO;

    public Room saveNewRoom(final Room room) throws BlankFieldsException, InvalidFieldException {
        chechData(room);
        if (room.getSensor().getId().equals("")){
            room.setSensor(null);
        }
        return roomDAO.save(room);
    }

    public Room updateRoom(final Room oldRoom, Room newData) throws BlankFieldsException, InvalidFieldException {
        chechData(newData);
        roomDAO.save(newData);
        return newData;
    }

    private void chechData(Room room) throws BlankFieldsException, InvalidFieldException {
        if(room.getNome().isEmpty()
                || room.getMaxTemp() == null
                || room.getMinTemp() == null
                || room.getAbsoluteMin() == null){
            throw new BlankFieldsException("Completa tutti i campi");
        }
        if (!(room.getMaxTemp().compareTo(room.getMinTemp()) > 0
                && room.getMinTemp().compareTo(room.getAbsoluteMin()) > 0
                && room.getAbsoluteMin().compareTo(BigDecimal.valueOf(0)) > 0
                && room.getAbsoluteMin().compareTo(BigDecimal.valueOf(10)) <= 0)
                && room.getMinTemp().compareTo(BigDecimal.valueOf(10)) > 0
                && room.getMaxTemp().compareTo(BigDecimal.valueOf(99)) <= 0){
            throw new InvalidFieldException("Controlla meglio i campi e riprova");
        }
    }
}
