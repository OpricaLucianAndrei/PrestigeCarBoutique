package it.capstone.prestigecarboutique.entity;


import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
public class Prenotazione {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"password", "ruolo", "pictureProfile"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "auto_id")
    @JsonIgnoreProperties(value = {"chilometraggio", "descrizione", "disponibilità", "statoVeicolo", "specifiche", "prezzo", "optionalFeatures", "prenotazioni", "immagini"})

    private Auto auto;

    @Column(name = "data_prenotazione")
    private LocalDate dataPrenotazione;

    @Column(name = "ora_prenotazione")
    private LocalTime oraPrenotazione;
}
