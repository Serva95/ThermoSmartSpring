package it.srv.ThermoSmartSpring.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "info")
public class Info{

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "time")
    private LocalDateTime time;

}
