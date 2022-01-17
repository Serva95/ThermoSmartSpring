package it.srv.ThermoSmartSpring.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "authorities")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Authorities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String authority;
    private String username;

    /*controllare
    org.postgresql.util.PSQLException: ERRORE: la colonna authoritie0_.authorities_id non esiste
    cercare query verbose
    */
    /*@ManyToOne(targetEntity = User.class, fetch=FetchType.LAZY)
    @JoinColumn(name = "username")
    private User user;*/

    public Authorities (String username, String authority){
        this.username = username;
        this.authority = authority;
    }
}
