package it.srv.ThermoSmartSpring.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrariOnOffString {
    private long id;
    private short giorno;
    private String orarioAccensioneA;
    private String orarioSpegnimentoA;
    private String orarioAccensioneB;
    private String orarioSpegnimentoB;
    private String orarioAccensioneC;
    private String orarioSpegnimentoC;
}
