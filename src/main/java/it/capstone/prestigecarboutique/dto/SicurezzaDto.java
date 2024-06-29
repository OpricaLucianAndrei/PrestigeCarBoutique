package it.capstone.prestigecarboutique.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SicurezzaDto {
    @NotNull(message = "Le informazioni sugli airbags non possono essere nulle")
    private AirbagsDto airbags;
    private List<String> assistenzaAllaGuida;
    @NotNull(message = "L'informazione sul airbag anteriore non può essere nulla")
    private List<String> sicurezza;
}
