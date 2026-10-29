package it.srv.ThermoSmartSpring.services

import it.srv.ThermoSmartSpring.dao.VmcDAO
import it.srv.ThermoSmartSpring.dto.VmcStringDTO
import it.srv.ThermoSmartSpring.exception.BlankFieldsException
import it.srv.ThermoSmartSpring.exception.InvalidFieldException
import it.srv.ThermoSmartSpring.exception.ObjectAlreadyExistException
import it.srv.ThermoSmartSpring.model.Vmc
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalTime
import java.time.format.DateTimeParseException

@Service
@Transactional
open class VmcService(
    private val vmcDAO: VmcDAO
) {

    private val idError = "L'id non può essere vuoto"
    private val idLengthError = "Id non valido, deve essere di 32 caratteri alfanumerici"

    @Throws(BlankFieldsException::class, InvalidFieldException::class)
    open fun saveNew(vmc: Vmc) {
        if (vmc.id.isEmpty())
            throw BlankFieldsException(idError)
        if (vmc.id.length != 32 || !vmc.id.matches(Regex("[a-zA-Z0-9]*")))
            throw InvalidFieldException(idLengthError)
        vmc.impostazioneFunzione = false
        vmc.statoAttuale = false
        vmcDAO.save(vmc)
    }

    @Throws(BlankFieldsException::class, InvalidFieldException::class, ObjectAlreadyExistException::class)
    open fun saveEditedVmc(vmc: VmcStringDTO, id: String) {
        val old = vmcDAO.get(id)
        val newVmc = Vmc(vmc.id)
        if (vmc.id.isEmpty())
            throw BlankFieldsException(idError)
        if (vmc.id.length != 32 || !vmc.id.matches(Regex("[a-zA-Z0-9]*")))
            throw InvalidFieldException(idLengthError)
        if (vmc.id != id && vmcDAO.exists(vmc.id))
            throw ObjectAlreadyExistException("Esiste già una vmc con questo id salvata, prova con uno diverso")
        
        newVmc.statoAttuale = old.statoAttuale
        newVmc.impostazioneFunzione = old.impostazioneFunzione
        
        try {
            val onTime = vmc.programmedOnTime
            newVmc.programmedOnTime = if (!onTime.isNullOrEmpty()) LocalTime.parse(onTime) else null
            val offTime = vmc.programmedOffTime
            newVmc.programmedOffTime = if (!offTime.isNullOrEmpty()) LocalTime.parse(offTime) else null
        } catch (ex: Exception) { // Catching Exception to cover both DateTimeParseException and NullPointerException as in original
            throw InvalidFieldException("Formato non valido per gli orari, controlla e riprova")
        }
        vmcDAO.update(newVmc, id)
    }

    open fun updateStatus(vmc: Vmc): Boolean {
        vmc.impostazioneFunzione = !(vmc.impostazioneFunzione ?: false)
        vmcDAO.save(vmc)
        return vmc.impostazioneFunzione ?: false
    }

    open fun delete(vmc: Vmc) {
        vmcDAO.delete(vmc.id)
    }
}
