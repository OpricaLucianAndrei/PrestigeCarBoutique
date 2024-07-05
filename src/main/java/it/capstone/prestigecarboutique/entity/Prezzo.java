package it.capstone.prestigecarboutique.entity;


import jakarta.persistence.*;
import lombok.Data;


import java.util.List;

@Entity
@Data
public class Prezzo {
    @Id
    @GeneratedValue
    private int id;

    private double prezzoBase;
    private double optional;
    private double tasse;
    private double prezzoTotale;
}
