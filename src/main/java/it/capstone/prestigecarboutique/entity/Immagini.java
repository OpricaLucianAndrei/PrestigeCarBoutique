package it.capstone.prestigecarboutique.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Immagini {
    @Id
    @GeneratedValue
    private int id;

    private String opzioneColore;
    @ElementCollection
    private List<String> url;
}

