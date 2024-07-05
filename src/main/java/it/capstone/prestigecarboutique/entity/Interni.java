package it.capstone.prestigecarboutique.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Interni {
    @Id
    @GeneratedValue
    private int id;

    private int posti;
    private String tappezzeria;

    @ElementCollection
    private List<String> caratteristiche;
}
