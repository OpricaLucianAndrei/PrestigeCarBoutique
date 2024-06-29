package it.capstone.prestigecarboutique.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OptionalFeatures {
    @Id
    @GeneratedValue
    private int id;
    private String nome;
    private double prezzo;
}
