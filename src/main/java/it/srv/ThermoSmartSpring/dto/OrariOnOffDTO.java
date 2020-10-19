package it.srv.ThermoSmartSpring.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class OrariOnOffDTO {
    private List<OrariOnOffStringDTO> orariOnOffs;

    public OrariOnOffDTO (){
        this.orariOnOffs = new ArrayList<>();
    }

    public void add(OrariOnOffStringDTO orariOnOff, short giorno) {
        orariOnOff.setGiorno(giorno);
        this.orariOnOffs.add(orariOnOff);
    }
}
