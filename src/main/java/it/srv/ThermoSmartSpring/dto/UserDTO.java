package it.srv.ThermoSmartSpring.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String matchingPassword;
    @NonNull
    private String email;
}
