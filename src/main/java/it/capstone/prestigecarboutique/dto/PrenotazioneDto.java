package it.capstone.prestigecarboutique.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class PrenotazioneDto {
    @NotNull(message = "L'userId non può essere nullo!")
    private int userId;
    @NotNull(message = "L'autoId non può essere nullo!")
    private int autoId;
    @NotNull(message = "La data della prenotazione non può essere nulla!")
    private LocalDate dataPrenotazione;
    @NotNull(message = "L'orario della prenotazione non può essere nullo!")
    private LocalTime oraPrenotazione;
}
