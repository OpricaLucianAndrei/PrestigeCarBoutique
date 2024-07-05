package it.capstone.prestigecarboutique.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Sicurezza {
    @Id
    @GeneratedValue
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "airbags_id")
    private Airbags airbags;

    @ElementCollection
    private List<String> assistenzaAllaGuida;

    @ElementCollection
    private List<String> sicurezza;
}
