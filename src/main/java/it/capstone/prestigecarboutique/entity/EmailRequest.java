package it.capstone.prestigecarboutique.entity;

import lombok.Data;

@Data
public class EmailRequest {
    private String subject;
    private String message;
}
