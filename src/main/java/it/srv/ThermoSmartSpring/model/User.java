package it.srv.ThermoSmartSpring.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "users")
@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NonNull
    private String email;
    @NonNull
    private String password;
    @NonNull
    private String username;
    /*@OneToMany(targetEntity = Authorities.class, cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinColumn(name = "username")
    private List<Authorities> authorities;*/
}
