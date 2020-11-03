package it.srv.ThermoSmartSpring.services;

import it.srv.ThermoSmartSpring.dao.RoomDAO;
import it.srv.ThermoSmartSpring.exception.BlankFieldsException;
import it.srv.ThermoSmartSpring.exception.InvalidFieldException;
import it.srv.ThermoSmartSpring.model.Room;
import it.srv.ThermoSmartSpring.model.Sensor;
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
        checkData(room);
        if (room.getSensor() == null || room.getSensor().getId().equals("")){
            room.setSensor(null);
        }
        return roomDAO.save(room);
    }

    public Room updateRoom(Room oldRoom, final Room newData) throws BlankFieldsException, InvalidFieldException {
        checkData(newData);
        oldRoom.setNome(newData.getNome());
        oldRoom.setMaxTemp(newData.getMaxTemp());
        oldRoom.setMinTemp(newData.getMinTemp());
        oldRoom.setAbsoluteMin(newData.getAbsoluteMin());
        if (newData.getSensor() == null || newData.getSensor().getId().equals("")){
            oldRoom.setSensor(null);
        } else {
            Sensor sensor = new Sensor();
            sensor.setId(newData.getSensor().getId());
            oldRoom.setSensor(sensor);
        }
        roomDAO.save(oldRoom);
        return newData;
    }

    public Room updateRoomStatus(Room room, Boolean ma, Boolean mi, Boolean mo){
        if(ma){
            room.setManualActive(!room.isManualActive());
        } else if (mi){
            room.setManualInactive(!room.isManualInactive());
        } else if (mo){
            room.setManualOff(!room.isManualOff());
        }
        return roomDAO.save(room);
    }

    private void checkData(Room room) throws BlankFieldsException, InvalidFieldException {
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
