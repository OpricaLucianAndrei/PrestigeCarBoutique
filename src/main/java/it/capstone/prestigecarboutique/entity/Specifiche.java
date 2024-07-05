package it.capstone.prestigecarboutique.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Specifiche {
    @Id
    @GeneratedValue
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "performance_id")
    private Performance performance;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dimensioni_id")
    private Dimensioni dimensioni;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "interni_id")
    private Interni interni;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "esterni_id")
    private Esterni esterni;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sicurezza_id")
    private Sicurezza sicurezza;
}
