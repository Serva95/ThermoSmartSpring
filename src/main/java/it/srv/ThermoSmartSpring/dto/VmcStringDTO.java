package it.srv.ThermoSmartSpring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VmcStringDTO {
    private String id;
    private Boolean statoAttuale;
    private Boolean impostazioneFunzione;
    private String programmedOnTime;
    private String programmedOffTime;
}
