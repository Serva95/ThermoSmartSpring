package it.srv.ThermoSmartSpring.model;

import lombok.*;

import javax.persistence.*;

@Table(name = "authorities")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Authorities {
    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NonNull
    private String username;
    @NonNull
    private String authority;

    public Authorities (String username, String authority){
        this.username = username;
        this.authority = authority;
    }
}
