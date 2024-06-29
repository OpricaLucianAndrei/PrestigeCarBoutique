package it.capstone.prestigecarboutique.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ConsumoCarburante {
    @Id
    @GeneratedValue
    private int id;

    private String città;
    private String autostrada;
    private String combinato;
}
