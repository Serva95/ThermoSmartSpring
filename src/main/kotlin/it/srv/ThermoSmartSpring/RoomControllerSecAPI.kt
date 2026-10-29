package it.srv.ThermoSmartSpring

import it.srv.ThermoSmartSpring.dao.RoomDAO
import it.srv.ThermoSmartSpring.dao.SensorDAO
import it.srv.ThermoSmartSpring.services.RoomService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/secure-api/rooms")
class RoomControllerSecAPI(
    private val roomDAO: RoomDAO,
    private val sensorDAO: SensorDAO,
    private val roomService: RoomService
) {

    @PutMapping("")
    fun allManualActive(@RequestParam(name = "status") status: String): String {
        var msg = "Si è verificato un errore, riprova"
        if (status.equals("on", ignoreCase = true)) {
            if (roomService.updateAllRooms(status))
                msg = "Tutte le stanze sono ora in modalità manuale massimo"
        } else if (status.equals("off", ignoreCase = true)) {
            if (roomService.updateAllRooms(status))
                msg = "Tutte le stanze sono ora in modalità automatica secondo gli orari"
        } else if (status.equals("alloff", ignoreCase = true)) {
            if (roomService.updateAllRooms(status))
                msg = "Il controllo è ora spento in tutte le stanze"
        }
        return msg
    }
}
