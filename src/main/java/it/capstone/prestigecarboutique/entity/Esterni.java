package it.capstone.prestigecarboutique.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Entity
@Data
public class Esterni {
    @Id
    @GeneratedValue
    private int id;

    @ElementCollection
    private List<String> opzioniColori;

    @ElementCollection
    private List<String> caratteristiche;

}
