package it.capstone.prestigecarboutique.dto;

import it.capstone.prestigecarboutique.enums.StatoVeicolo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AutoDto {
    @NotNull(message = "L'informazione sulla marca non può essere nulla")
    private String marca;
    @NotNull(message = "L'informazione sul modello non può essere nulla")
    private String modello;
    @Min(1900)
    private int anno;
    @NotNull(message = "L'informazione sul chilometraggio non può essere nulla")
    private int chilometraggio;
    @NotNull(message = "La descrizione non può essere nulla")
    private String descrizione;
    @NotNull(message = "L'informazione sulla disponibilità non può essere nulla")
    private boolean disponibilità;
    @NotNull(message = "L'informazione sullo stato dell'auto non può essere nulla")
    private StatoVeicolo statoVeicolo;
    @NotNull(message = "Le informazioni sulle specifiche non possono essere nulle")
    private SpecificheDto specifiche;
    @NotNull(message = "Le informazioni sui prezzi non possono essere nulle")
    private PrezzoDto prezzo;
    private List<OptionalFeaturesDto> optionalFeatures;
    private List<ImmaginiDto> immagini;
}
