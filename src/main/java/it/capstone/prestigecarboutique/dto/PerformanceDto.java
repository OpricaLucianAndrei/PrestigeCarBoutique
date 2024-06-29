package it.capstone.prestigecarboutique.dto;

import it.capstone.prestigecarboutique.entity.ConsumoCarburante;
import it.capstone.prestigecarboutique.entity.Motore;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PerformanceDto {
    private MotoreDto motore;
    private String topSpeed;
    private String accelerazione0a100Kmh;
    private ConsumoCarburanteDto consumoCarburante;
    private String emissioni;
}
