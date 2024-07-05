package it.capstone.prestigecarboutique.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class InterniDto {
    @Min(1)
    private int posti;
    @NotNull(message = "Le informazioni sulla tappezzeria non possono essere nulle")
    private String tappezzeria;

    private List<String> caratteristiche;
}
