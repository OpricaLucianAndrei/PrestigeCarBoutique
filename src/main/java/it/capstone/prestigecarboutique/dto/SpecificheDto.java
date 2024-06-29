package it.capstone.prestigecarboutique.dto;

import it.capstone.prestigecarboutique.entity.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SpecificheDto {
    @NotNull(message = "Le informazioni sulle performance non possono essere nulle")
    private PerformanceDto performance;
    @NotNull(message = "Le informazioni sulle dimensioni non possono essere nulle")
    private DimensioniDto dimensioni;
    @NotNull(message = "Le informazioni sugli interni non possono essere nulle")
    private InterniDto interni;
    @NotNull(message = "Le informazioni sugli esterni non possono essere nulle")
    private EsterniDto esterni;
    @NotNull(message = "Le informazioni sulla sicurezza non possono essere nulle")
    private SicurezzaDto sicurezza;

}
