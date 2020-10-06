package it.srv.ThermoSmartSpring.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
}