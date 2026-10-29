package it.srv.ThermoSmartSpring.services

import it.srv.ThermoSmartSpring.dao.RoomDAO
import it.srv.ThermoSmartSpring.exception.BlankFieldsException
import it.srv.ThermoSmartSpring.exception.InvalidFieldException
import it.srv.ThermoSmartSpring.model.Room
import it.srv.ThermoSmartSpring.model.Sensor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
@Transactional
open class RoomService(
    private val roomDAO: RoomDAO
) {

    @Throws(BlankFieldsException::class, InvalidFieldException::class)
    open fun saveNewRoom(room: Room) {
        checkData(room)
        if (room.sensor == null || room.sensor?.id?.isEmpty() == true) {
            room.sensor = null
        }
        roomDAO.save(room)
    }

    @Throws(BlankFieldsException::class, InvalidFieldException::class)
    open fun updateRoom(oldRoom: Room, newData: Room) {
        checkData(newData)
        oldRoom.nome = newData.nome
        oldRoom.maxTemp = newData.maxTemp
        oldRoom.minTemp = newData.minTemp
        oldRoom.absoluteMin = newData.absoluteMin
        if (newData.sensor == null || newData.sensor?.id?.isEmpty() == true) {
            oldRoom.sensor = null
        } else {
            val sensor = Sensor()
            sensor.id = newData.sensor!!.id
            oldRoom.sensor = sensor
        }
        roomDAO.save(oldRoom)
    }

    open fun updateAllRooms(status: String): Boolean {
        val rooms = roomDAO.getAll(true)
        var ma = false
        var mi = false
        var mo = false
        try {
            if (status.equals("on", ignoreCase = true)) {
                ma = true
                mi = false
                mo = false
            } else if (status.equals("alloff", ignoreCase = true)) {
                mo = true
                ma = false
                mi = false
            }
            for (r in rooms) {
                r.manualActive = ma
                r.manualInactive = mi
                r.manualOff = mo
                roomDAO.save(r)
            }
        } catch (e: Exception) {
            return false
        }
        return true
    }

    open fun updateRoomStatus(room: Room, ma: Boolean?, mi: Boolean?, mo: Boolean?) {
        if (ma == true) {
            room.manualActive = !room.manualActive
        } else if (mi == true) {
            room.manualInactive = !room.manualInactive
        } else if (mo == true) {
            room.manualOff = !room.manualOff
        }
        roomDAO.save(room)
    }

    @Throws(BlankFieldsException::class, InvalidFieldException::class)
    private fun checkData(room: Room) {
        if (room.nome.isNullOrEmpty()
            || room.maxTemp == null
            || room.minTemp == null
            || room.absoluteMin == null
        ) {
            throw BlankFieldsException("Completa tutti i campi")
        }
        if (!(room.maxTemp!!.compareTo(room.minTemp!!) > 0
            && room.minTemp!!.compareTo(room.absoluteMin!!) > 0
            && room.absoluteMin!!.compareTo(BigDecimal.ZERO) > 0
            && room.absoluteMin!!.compareTo(BigDecimal.valueOf(10)) <= 0)
            && room.minTemp!!.compareTo(BigDecimal.valueOf(10)) > 0
            && room.maxTemp!!.compareTo(BigDecimal.valueOf(99)) <= 0
        ) {
            throw InvalidFieldException("Controlla meglio i campi e riprova")
        }
    }
}
