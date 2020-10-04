package it.srv.ThermoSmartSpring.dto;

import it.srv.ThermoSmartSpring.model.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String matchingPassword;
    @NonNull
    private String email;
    private String oldPassword;

    public User userDTOtoUser (){
        User user = new User();
        user.setEmail(this.getEmail());
        user.setUsername(this.getUsername());
        user.setPassword(this.getPassword());
        return user;
    }
}
