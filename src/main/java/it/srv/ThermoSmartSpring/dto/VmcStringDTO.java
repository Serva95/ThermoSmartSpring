package it.srv.ThermoSmartSpring.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VmcStringDTO {
    private String id;
    private Boolean statoAttuale;
    private Boolean impostazioneFunzione;
    private String programmedOnTime;
    private String programmedOffTime;
}
