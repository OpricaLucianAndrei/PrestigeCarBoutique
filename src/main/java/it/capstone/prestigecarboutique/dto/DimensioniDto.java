package it.capstone.prestigecarboutique.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DimensioniDto {
    @NotNull(message = "L'informazione sulla lunghezza non può essere nulla")
    private String lunghezza;

    @NotNull(message = "L'informazione sulla larghezza non può essere nulla")
    private String larghezza;

    @NotNull(message = "L'informazione sulla altezza non può essere nulla")
    private String altezza;

    @NotNull(message = "L'informazione sul passo non può essere nulla")
    private String passo;

    @NotNull(message = "L'informazione sul peso a vuoto non può essere nulla")
    private String pesoAVuoto;

    @NotNull(message = "L'informazione sulla capacità del serbatoio non può essere nulla")
    private String capacitaSerbatoio;
}
