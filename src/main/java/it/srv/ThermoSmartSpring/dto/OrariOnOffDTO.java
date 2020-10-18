package it.srv.ThermoSmartSpring.dto;

import it.srv.ThermoSmartSpring.model.OrariOnOff;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class OrariOnOffDTO {
    private List<OrariOnOff> orariOnOffs;

    public OrariOnOffDTO (){
        this.orariOnOffs = new ArrayList<>();
    }

    public void add(OrariOnOff orariOnOff) {
        this.orariOnOffs.add(orariOnOff);
    }
}
