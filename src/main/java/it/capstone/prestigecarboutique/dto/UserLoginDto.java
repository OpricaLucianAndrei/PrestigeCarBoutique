package it.capstone.prestigecarboutique.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginDto {

    @Email(message = "Inserire l'email")
    @NotBlank
    private String email;

    @NotBlank(message = "Inserire la password")
    private String password;
}
