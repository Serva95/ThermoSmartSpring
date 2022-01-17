package it.srv.ThermoSmartSpring.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Table(name = "sensors")
@Entity
@Getter @Setter
@NoArgsConstructor
public class Sensor {
    @Id
    @NonNull
    private String id;
    @NonNull
    private String nome;
    @NonNull
    private String location;

    @OneToOne(mappedBy = "sensor")
    private Room room;

    @OneToMany(mappedBy = "sensor")
    private List<Temp> temps;
}