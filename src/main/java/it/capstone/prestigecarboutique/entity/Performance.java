package it.capstone.prestigecarboutique.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Performance {
    @Id
    @GeneratedValue
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "motore_id")
    private Motore motore;

    private String topSpeed;
    private String accelerazione0a100Kmh;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "consumo_carburante_id")
    private ConsumoCarburante consumoCarburante;

    private String emissioni;
}
