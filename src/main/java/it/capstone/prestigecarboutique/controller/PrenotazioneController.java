package it.capstone.prestigecarboutique.controller;


import it.capstone.prestigecarboutique.dto.AutoDto;
import it.capstone.prestigecarboutique.dto.PrenotazioneDto;
import it.capstone.prestigecarboutique.entity.Prenotazione;
import it.capstone.prestigecarboutique.exception.BadRequestException;
import it.capstone.prestigecarboutique.service.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestigecarboutique")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    @PostMapping("/prenotazioni")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('AMMINISTRATORE', 'CLIENTE')")
    public String savePrenotazione(@RequestBody @Validated PrenotazioneDto prenotazioneDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors()
                    .stream().map(e -> e.getDefaultMessage()).reduce("", (s, a) -> s + a));
        }
        return prenotazioneService.creaPrenotazione(prenotazioneDto);
    }

    @GetMapping("/prenotazioni")
    @PreAuthorize("hasAnyAuthority('AMMINISTRATORE', 'CLIENTE')")
    public List<Prenotazione> getPrenotazioni() {
        return prenotazioneService.getPrenotazioni();
    }

    @GetMapping("/prenotazioni/{id}")
    @PreAuthorize("hasAnyAuthority('AMMINISTRATORE', 'CLIENTE')")
    public Prenotazione getPrenotazioneById(@PathVariable int id) {
        return prenotazioneService.getPrenotazioneById(id);
    }

    @PutMapping("/prenotazioni/{id}")
    @PreAuthorize("hasAnyAuthority('AMMINISTRATORE', 'CLIENTE')")
    public Prenotazione updatePrenotazione(@PathVariable int id, @RequestBody @Validated PrenotazioneDto prenotazioneDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors()
                   .stream().map(e -> e.getDefaultMessage()).reduce("", (s, a) -> s + a));
        }
        return prenotazioneService.updatePrenotazione(id, prenotazioneDto);
    }

    @DeleteMapping("/prenotazioni/{id}")
    @PreAuthorize("hasAnyAuthority('AMMINISTRATORE', 'CLIENTE')")
    public String deletePrenotazione(@PathVariable int id) {
        return prenotazioneService.deletePrenotazione(id);
    }
}
