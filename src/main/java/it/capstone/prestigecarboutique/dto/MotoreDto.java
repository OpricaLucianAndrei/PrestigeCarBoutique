package it.capstone.prestigecarboutique.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MotoreDto {
    @NotNull(message = "L'informazione sul tipo di motore non può essere nulla")
    private String tipo;
    @NotNull(message = "L'informazione sul dislocamento del motore non può essere nulla")
    private String cilindrata;
    @NotNull(message = "L'informazione sui cavalli del motore non può essere nulla")
    private int cavalli;
    @NotNull(message = "L'informazione sulla coppia del motore non può essere nulla")
    private String coppia;
    @NotNull(message = "L'informazione sulla trasmissione non può essere nulla")
    private String trasmissione;
    @NotNull(message = "L'informazione sulla trazione non può essere nulla")
    private String trazione;
}
