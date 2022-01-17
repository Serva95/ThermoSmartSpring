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

    public void saveNewRoom(final Room room) throws BlankFieldsException, InvalidFieldException {
        checkData(room);
        if (room.getSensor() == null || room.getSensor().getId().equals("")){
            room.setSensor(null);
        }
        roomDAO.save(room);
    }

    public void updateRoom(Room oldRoom, final Room newData) throws BlankFieldsException, InvalidFieldException {
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
    }

    public boolean updateAllRooms(String status){
        Iterable<Room> rooms = roomDAO.getAll(true);
        boolean ma = false;
        boolean mi = false;
        boolean mo = false;
        try {
            if(status.equalsIgnoreCase("on")){
                ma = true;
                mi = mo = false;
            } else if(status.equalsIgnoreCase("alloff")){
                mo = true;
                ma = mi = false;
            }
            for (Room r : rooms) {
                r.setManualActive(ma);
                r.setManualInactive(mi);
                r.setManualOff(mo);
                roomDAO.save(r);
            }
        } catch (Exception ignored){
            return false;
        }
        return true;
    }

    public void updateRoomStatus(Room room, Boolean ma, Boolean mi, Boolean mo){
        if(ma){
            room.setManualActive(!room.isManualActive());
        } else if (mi){
            room.setManualInactive(!room.isManualInactive());
        } else if (mo){
            room.setManualOff(!room.isManualOff());
        }
        roomDAO.save(room);
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
