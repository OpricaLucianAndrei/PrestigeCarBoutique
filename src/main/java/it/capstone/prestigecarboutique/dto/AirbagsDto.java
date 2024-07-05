package it.capstone.prestigecarboutique.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AirbagsDto {
    @NotNull(message = "L'informazione sul airbag anteriore non può essere nulla")
    private boolean anteriore;
    @NotNull(message = "L'informazione sul airbag laterale non può essere nulla")
    private boolean laterale;
    @NotNull(message = "L'informazione sul airbag tendina non può essere nulla")
    private boolean tendina;
    @NotNull(message = "L'informazione sul airbag ginocchia non può essere nulla")
    private boolean ginocchia;
}
