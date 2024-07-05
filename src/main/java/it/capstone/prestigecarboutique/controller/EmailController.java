package it.capstone.prestigecarboutique.controller;

import it.capstone.prestigecarboutique.entity.EmailRequest;
import it.capstone.prestigecarboutique.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/prestigecarboutique")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-email")
    public String sendEmail(@RequestBody EmailRequest emailRequest, @RequestParam String senderEmail) {
        emailService.sendEmail(senderEmail, "concessionario.pcb@gmail.com", emailRequest.getSubject(), emailRequest.getMessage());
        return "Email inviata con successo!";
    }
}
