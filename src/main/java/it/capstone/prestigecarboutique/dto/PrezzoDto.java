package it.capstone.prestigecarboutique.dto;


import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.List;

@Data
public class PrezzoDto {
    @Min(value = 0)
    private double prezzoBase;
    private double optional;
    private double tasse;
    private double prezzoTotale;


}
