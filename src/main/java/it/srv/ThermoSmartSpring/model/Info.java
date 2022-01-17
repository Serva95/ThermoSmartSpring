package it.srv.ThermoSmartSpring.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
