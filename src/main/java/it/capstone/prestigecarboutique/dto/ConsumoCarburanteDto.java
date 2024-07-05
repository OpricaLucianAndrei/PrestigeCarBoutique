package it.capstone.prestigecarboutique.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConsumoCarburanteDto {
    @NotNull(message = "L'informazione sul consumo in città non può essere nulla")
    private String città;
    @NotNull(message = "L'informazione sul consumo in autostrada non può essere nulla")
    private String autostrada;
    @NotNull(message = "L'informazione sul consumo combinato non può essere nulla")
    private String combinato;

}
