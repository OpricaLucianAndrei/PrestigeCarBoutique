package it.capstone.prestigecarboutique.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Airbags {
    @Id
    @GeneratedValue
    private int id;

    private boolean anteriore;
    private boolean laterale;
    private boolean tendina;
    private boolean ginocchia;

}
