package it.capstone.prestigecarboutique.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.capstone.prestigecarboutique.entity.User;
import lombok.Data;

@Data
public class AuthenticationResponse {

    private String token;

    @JsonIgnoreProperties(value = "password")
    private User user;

    public AuthenticationResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }
}
