package it.srv.ThermoSmartSpring.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Table(name = "users")
@Entity
@Getter
@Setter
public class User {
    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NonNull
    private String email;
    @NonNull
    private String password;
    @NonNull
    private String username;
}
