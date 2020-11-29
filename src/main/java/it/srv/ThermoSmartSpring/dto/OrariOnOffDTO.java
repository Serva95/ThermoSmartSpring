package it.srv.ThermoSmartSpring.dto;

import it.srv.ThermoSmartSpring.model.OrariOnOff;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class OrariOnOffDTO {
    private List<OrariOnOffString> orariOnOffs;

    public OrariOnOffDTO (){
        this.orariOnOffs = new ArrayList<>();
    }

    public void add(final OrariOnOffString orariOnOff, final short giorno) {
        orariOnOff.setGiorno(giorno);
        this.orariOnOffs.add(orariOnOff);
    }

    /**
     * Converte una lista di 7 elementi di tipo OrariOnOff, quindi con tipo LocalTime per gli orari,
     * in tipo OrariOnOffStringDTO con tipo String per gli orari, in modo da facilitare la creazione
     * e la successiva lettura nelle pagine html che usano tale lista
     * @param orariOnOffs di tipo List&lt;OrariOnOff&gt; è la lista di 7 elementi da convertire
     */
    public void orariToStringDTO (final List<OrariOnOff> orariOnOffs){
        ArrayList<OrariOnOffString> orari = new ArrayList<>();
        for (OrariOnOff e : orariOnOffs) {
            OrariOnOffString stringDTO = new OrariOnOffString();
            stringDTO.setId(e.getId());
            stringDTO.setGiorno(e.getGiorno());
            stringDTO.setOrarioAccensioneA(e.getOrarioAccensioneA().toString());
            stringDTO.setOrarioSpegnimentoA(e.getOrarioSpegnimentoA().toString());
            if (e.getOrarioAccensioneB() != null) {
                stringDTO.setOrarioAccensioneB(e.getOrarioAccensioneB().toString());
                stringDTO.setOrarioSpegnimentoB(e.getOrarioSpegnimentoB().toString());
            }
            if (e.getOrarioAccensioneC() != null) {
                stringDTO.setOrarioAccensioneC(e.getOrarioAccensioneC().toString());
                stringDTO.setOrarioSpegnimentoC(e.getOrarioSpegnimentoC().toString());
            }
            orari.add(stringDTO);
        }
        this.orariOnOffs = orari;
    }
}
