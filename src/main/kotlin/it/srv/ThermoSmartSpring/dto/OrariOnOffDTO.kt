package it.srv.ThermoSmartSpring.dto

import it.srv.ThermoSmartSpring.model.OrariOnOff
import java.util.ArrayList

class OrariOnOffDTO {
    var orariOnOffs: MutableList<OrariOnOffString> = ArrayList()

    fun add(orariOnOff: OrariOnOffString, giorno: Short) {
        orariOnOff.giorno = giorno
        this.orariOnOffs.add(orariOnOff)
    }

    /**
     * Converte una lista di 7 elementi di tipo OrariOnOff, quindi con tipo LocalTime per gli orari,
     * in tipo OrariOnOffStringDTO con tipo String per gli orari, in modo da facilitare la creazione
     * e la successiva lettura nelle pagine html che usano tale lista
     * @param orariOnOffs di tipo List<OrariOnOff> è la lista di 7 elementi da convertire
     */
    fun orariToStringDTO(orariOnOffs: List<OrariOnOff>) {
        val orari = ArrayList<OrariOnOffString>()
        for (e in orariOnOffs) {
            val stringDTO = OrariOnOffString()
            stringDTO.id = e.id
            stringDTO.giorno = e.giorno
            stringDTO.orarioAccensioneA = e.orarioAccensioneA.toString()
            stringDTO.orarioSpegnimentoA = e.orarioSpegnimentoA.toString()
            if (e.orarioAccensioneB != null) {
                stringDTO.orarioAccensioneB = e.orarioAccensioneB.toString()
                stringDTO.orarioSpegnimentoB = e.orarioSpegnimentoB.toString()
            }
            if (e.orarioAccensioneC != null) {
                stringDTO.orarioAccensioneC = e.orarioAccensioneC.toString()
                stringDTO.orarioSpegnimentoC = e.orarioSpegnimentoC.toString()
            }
            orari.add(stringDTO)
        }
        this.orariOnOffs = orari
    }
}
