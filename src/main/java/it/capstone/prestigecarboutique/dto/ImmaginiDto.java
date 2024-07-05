package it.capstone.prestigecarboutique.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ImmaginiDto {
    @NotNull(message = "Le opzioni delle colorazioni non possono essere nulle")
    private String opzioneColore;
    @NotNull(message = "I url delle immagini non possono essere nulle")
    private List<String> url;
}
