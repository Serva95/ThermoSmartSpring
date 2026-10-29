package it.srv.ThermoSmartSpring.services

import it.srv.ThermoSmartSpring.dao.OrariOnOffDAO
import it.srv.ThermoSmartSpring.dao.RoomDAO
import it.srv.ThermoSmartSpring.dto.OrariOnOffDTO
import it.srv.ThermoSmartSpring.dto.OrariOnOffString
import it.srv.ThermoSmartSpring.exception.BlankFieldsException
import it.srv.ThermoSmartSpring.exception.InvalidFieldException
import it.srv.ThermoSmartSpring.exception.ObjectAlreadyExistException
import it.srv.ThermoSmartSpring.model.OrariOnOff
import it.srv.ThermoSmartSpring.model.Room
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalTime
import java.time.format.DateTimeParseException

@Service
@Transactional
open class OrariOnOffService(
    private val orariOnOffDAO: OrariOnOffDAO,
    private val roomDAO: RoomDAO
) {

    @Throws(BlankFieldsException::class, InvalidFieldException::class, ObjectAlreadyExistException::class)
    open fun saveOrari(orari: OrariOnOffDTO, id: Long) {
        val room = roomDAO.get(id)
        val toSave = mutableListOf<OrariOnOff>()
        orariObjectConverter(orari, room, toSave, null)
        val orariOnOffs = orariOnOffDAO.getByRoomId(id)
        if (orariOnOffs.iterator().hasNext()) {
            throw ObjectAlreadyExistException("Esistono già degli orari per questa stanza, non inserirne altri")
        }
        orariOnOffDAO.saveAll(toSave)
    }

    @Throws(BlankFieldsException::class, InvalidFieldException::class)
    open fun updateOrari(orari: OrariOnOffDTO, id: Long) {
        val room = roomDAO.get(id)
        val toSave = mutableListOf<OrariOnOff>()
        val orariOnOffs = orariOnOffDAO.getByRoomId(id)
        orariObjectConverter(orari, room, toSave, orariOnOffs)
        orariOnOffDAO.saveAll(toSave)
    }

    @Throws(BlankFieldsException::class, InvalidFieldException::class)
    private fun orariObjectConverter(
        orari: OrariOnOffDTO,
        room: Room,
        toSave: MutableList<OrariOnOff>,
        orariOnOffs: List<OrariOnOff>?
    ) {
        val msg1 = "L'orario di accensione non può essere successivo a quello di spegnimento, riprova."
        val msg2 = "L'orario di accensione di una fascia deve essere successivo a quello di spegnimento della fascia precedente, riprova."
        var iter: Iterator<OrariOnOff>? = null
        if (orariOnOffs != null) {
            iter = orariOnOffs.iterator()
        }
        for (e in orari.orariOnOffs) {
            val tmp = OrariOnOff()
            if (orariOnOffs != null && iter != null && iter.hasNext()) {
                tmp.id = iter.next().id
            }
            tmp.giorno = e.giorno.toShort()
            tmp.room = room
            try {
                val accA = e.orarioAccensioneA
                val speA = e.orarioSpegnimentoA
                val accB = e.orarioAccensioneB
                val speB = e.orarioSpegnimentoB
                val accC = e.orarioAccensioneC
                val speC = e.orarioSpegnimentoC

                if (accA.isNullOrEmpty() || speA.isNullOrEmpty()) {
                    throw BlankFieldsException("Completa tutti i campi e riprova")
                }

                tmp.orarioAccensioneA = LocalTime.parse(accA)
                tmp.orarioSpegnimentoA = LocalTime.parse(speA)

                if (tmp.orarioAccensioneA!!.isAfter(tmp.orarioSpegnimentoA!!)) {
                    throw InvalidFieldException(msg1)
                }

                if (!accB.isNullOrEmpty() && !speB.isNullOrEmpty()) {
                    tmp.orarioAccensioneB = LocalTime.parse(accB)
                    tmp.orarioSpegnimentoB = LocalTime.parse(speB)

                    if (tmp.orarioAccensioneB!!.isAfter(tmp.orarioSpegnimentoB!!)) {
                        throw InvalidFieldException(msg1)
                    }
                    if (tmp.orarioSpegnimentoA!!.isAfter(tmp.orarioAccensioneB!!)) {
                        throw InvalidFieldException(msg2)
                    }

                    if (!accC.isNullOrEmpty() && !speC.isNullOrEmpty()) {
                        tmp.orarioAccensioneC = LocalTime.parse(accC)
                        tmp.orarioSpegnimentoC = LocalTime.parse(speC)

                        if (tmp.orarioAccensioneC!!.isAfter(tmp.orarioSpegnimentoC!!)) {
                            throw InvalidFieldException(msg1)
                        }
                        if (tmp.orarioSpegnimentoB!!.isAfter(tmp.orarioAccensioneC!!)) {
                            throw InvalidFieldException(msg2)
                        }
                    }
                }
            } catch (ex: DateTimeParseException) {
                throw InvalidFieldException("Formato non valido per gli orari, controlla e riprova")
            }
            toSave.add(tmp)
        }
    }
}
