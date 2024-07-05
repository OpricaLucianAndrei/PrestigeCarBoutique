package it.capstone.prestigecarboutique.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class EsterniDto {
    @NotNull(message = "Le informazioni sulle opzioni dei colori non possono essere nulle")
    private List<String> opzioniColori;

    private List<String> caratteristiche;
}
