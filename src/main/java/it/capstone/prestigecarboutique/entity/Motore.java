package it.capstone.prestigecarboutique.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Motore {
    @Id
    @GeneratedValue
    private int id;

    private String tipo;
    private String cilindrata;
    private int cavalli;
    private String coppia;
    private String trasmissione;
    private String trazione;
}
