package it.srv.ThermoSmartSpring

import it.srv.ThermoSmartSpring.dao.RoomDAO
import it.srv.ThermoSmartSpring.dao.SensorDAO
import it.srv.ThermoSmartSpring.exception.BlankFieldsException
import it.srv.ThermoSmartSpring.exception.InvalidFieldException
import it.srv.ThermoSmartSpring.model.Room
import it.srv.ThermoSmartSpring.services.RoomService
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@RestController
@RequestMapping("/rooms")
class RoomController(
    private val roomDAO: RoomDAO,
    private val sensorDAO: SensorDAO,
    private val roomService: RoomService
) {

    @GetMapping("")
    fun Rooms(mav: ModelAndView): ModelAndView {
        mav.setViewName("rooms")
        mav.addObject("rooms", roomDAO.getAll(true))
        return mav
    }

    @GetMapping("/new")
    fun newRoom(mav: ModelAndView): ModelAndView {
        mav.addObject("room", Room())
        mav.addObject("sensors", sensorDAO.findAllRoomIsNull())
        mav.setViewName("newRoom")
        return mav
    }

    @PostMapping("/new")
    fun createRoom(mav: ModelAndView, @ModelAttribute("room") room: Room): ModelAndView {
        try {
            roomService.saveNewRoom(room)
            mav.addObject("message", "Stanza creata con successo.")
            mav.setViewName("redirect:/rooms")
            return mav
        } catch (e: Exception) {
            when (e) {
                is BlankFieldsException, is InvalidFieldException -> {
                    mav.addObject("message", e.message)
                    mav.addObject("room", room)
                    mav.addObject("sensors", sensorDAO.findAllRoomIsNull())
                    mav.setViewName("newRoom")
                    return mav
                }
                else -> throw e
            }
        }
    }

    @GetMapping("/{id}")
    fun viewRoom(mav: ModelAndView, @PathVariable id: Long): ModelAndView {
        val room = roomDAO.get(id)
        if (room == null) {
            mav.addObject("message", "Stanza non trovata, riprova.")
            mav.setViewName("redirect:/rooms")
            return mav
        }
        mav.addObject("room", room)
        mav.setViewName("viewRoom")
        return mav
    }

    @GetMapping("/{id}/edit")
    fun editRoom(mav: ModelAndView, @PathVariable id: Long): ModelAndView {
        val room = roomDAO.get(id)
        if (room == null) {
            mav.addObject("message", "Stanza non trovata, riprova.")
            mav.setViewName("redirect:/rooms")
            return mav
        }
        if (room.sensor != null) {
            mav.addObject("sensors", sensorDAO.findNotAssociatedPlusActual(room.sensor!!.id))
        } else {
            mav.addObject("sensors", sensorDAO.findAllRoomIsNull())
        }
        mav.addObject("room", room)
        mav.setViewName("editRoom")
        return mav
    }

    @PutMapping("/{id}")
    fun updateRoom(mav: ModelAndView, @PathVariable id: Long, @ModelAttribute("room") room: Room): ModelAndView {
        val oldRoom = roomDAO.get(id)
        if (oldRoom == null) {
            mav.addObject("message", "Stanza non trovata, riprova.")
            mav.setViewName("redirect:/rooms")
            return mav
        }
        try {
            roomService.updateRoom(oldRoom, room)
            mav.addObject("message", "Stanza modificata con successo.")
            mav.setViewName("redirect:/rooms")
            return mav
        } catch (e: Exception) {
            when (e) {
                is BlankFieldsException, is InvalidFieldException -> {
                    mav.addObject("message", e.message)
                    mav.addObject("room", room)
                    if (room.sensor != null) {
                        mav.addObject("sensors", sensorDAO.findNotAssociatedPlusActual(oldRoom.sensor?.id ?: ""))
                    } else {
                        mav.addObject("sensors", sensorDAO.findAllRoomIsNull())
                    }
                    mav.setViewName("editRoom")
                    return mav
                }
                else -> throw e
            }
        }
    }

    @PutMapping("/{id}/manualActive")
    fun updateRoomManAct(mav: ModelAndView, @PathVariable id: Long): ModelAndView {
        val room = roomDAO.get(id)
        if (room == null) {
            mav.addObject("message", "Stanza non trovata, riprova.")
            mav.setViewName("redirect:/rooms")
            return mav
        }
        roomService.updateRoomStatus(room, true, false, false)
        mav.addObject("message", "Stato modificato con successo.")
        mav.setViewName("redirect:/rooms/$id")
        return mav
    }

    @PutMapping("/{id}/manualInactive")
    fun updateRoomManInact(mav: ModelAndView, @PathVariable id: Long): ModelAndView {
        val room = roomDAO.get(id)
        if (room == null) {
            mav.addObject("message", "Stanza non trovata, riprova.")
            mav.setViewName("redirect:/rooms")
            return mav
        }
        roomService.updateRoomStatus(room, false, true, false)
        mav.addObject("message", "Stato modificato con successo.")
        mav.setViewName("redirect:/rooms/$id")
        return mav
    }

    @PutMapping("/{id}/manualOff")
    fun updateRoomManOff(mav: ModelAndView, @PathVariable id: Long): ModelAndView {
        val room = roomDAO.get(id)
        if (room == null) {
            mav.addObject("message", "Stanza non trovata, riprova.")
            mav.setViewName("redirect:/rooms")
            return mav
        }
        roomService.updateRoomStatus(room, false, false, true)
        mav.addObject("message", "Stato modificato con successo.")
        mav.setViewName("redirect:/rooms/$id")
        return mav
    }

    @DeleteMapping("/{id}")
    fun deleteRoom(mav: ModelAndView, @PathVariable id: Long): ModelAndView {
        val room = roomDAO.get(id)
        try {
            roomDAO.delete(room.id)
            mav.addObject("message", "Stanza eliminata con successo.")
        } catch (e: NullPointerException) {
            mav.addObject("message", "Stanza non trovata, riprova.")
        }
        mav.setViewName("redirect:/rooms")
        return mav
    }
}
