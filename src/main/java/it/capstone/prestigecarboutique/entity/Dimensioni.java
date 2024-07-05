package it.capstone.prestigecarboutique.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Dimensioni {
    @Id
    @GeneratedValue
    private int id;

    private String lunghezza;
    private String larghezza;
    private String altezza;
    private String passo;
    private String pesoAVuoto;
    private String capacitaSerbatoio;
}
