package it.capstone.prestigecarboutique.entity;


import it.capstone.prestigecarboutique.enums.StatoVeicolo;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Auto {
    @Id
    @GeneratedValue
    private int id;

    private String marca;
    private String modello;
    private int anno;
    private int chilometraggio;
    private String descrizione;
    private boolean disponibilità;
    @Enumerated(EnumType.STRING)
    private StatoVeicolo statoVeicolo;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "specifiche_id")
    private  Specifiche specifiche;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "prezzo_id")
    private Prezzo prezzo;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "optional_features_id")
    private List<OptionalFeatures> optionalFeatures;

    @OneToMany(mappedBy = "auto")
    private List<Prenotazione> prenotazioni;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "immagini_id")
    private List<Immagini> immagini;
}
