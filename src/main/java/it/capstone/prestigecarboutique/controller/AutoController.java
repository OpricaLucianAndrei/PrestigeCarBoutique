package it.capstone.prestigecarboutique.controller;


import it.capstone.prestigecarboutique.dto.*;
import it.capstone.prestigecarboutique.entity.Auto;
import it.capstone.prestigecarboutique.exception.BadRequestException;
import it.capstone.prestigecarboutique.exception.NotFoundException;
import it.capstone.prestigecarboutique.service.AutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/prestigecarboutique")
public class AutoController {

    @Autowired
    private AutoService autoService;

    @PostMapping("/auto")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('AMMINISTRATORE')")
    public String saveAuto(@RequestBody @Validated AutoDto autoDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors()
                    .stream().map(e -> e.getDefaultMessage()).reduce("", (s, a) -> s + a));
        }
        return autoService.saveAuto(autoDto);
    }

    @GetMapping("/allauto")
    public List<Auto> getTutteLeAuto(){
        return autoService.trovaTutteLeAuto();
    }

    @GetMapping("/auto")
    public Page<Auto> getAuto(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "id") String sortBy,
                                    @RequestParam(defaultValue = "desc") String sortOrder) {
        return autoService.getAutoConPaginazione(page, size, sortBy,sortOrder);
    }

    @GetMapping("/auto/{id}")
    @PreAuthorize("hasAnyAuthority('AMMINISTRATORE', 'CLIENTE')")
    public Auto getAutoById(@PathVariable int id) {
        Optional<Auto> autoOptional = autoService.getAutoById(id);
        if (autoOptional.isPresent()) {
            return autoOptional.get();
        } else {
            throw new NotFoundException("Auto con id " + id +" non trovato");
        }
    }
    @PutMapping("/auto/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String updateAuto(@PathVariable int id, @RequestBody @Validated AutoDto autoDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream().
                    map(objectError -> objectError.getDefaultMessage()).reduce("", ((s, s2) -> s + s2)));
        }

        return autoService.updateAuto(id, autoDto);
    }

    @DeleteMapping("/auto/{id}")
    @PreAuthorize("hasAuthority('AMMINISTRATORE')")
    public String deleteAuto(@PathVariable int id) {
        return autoService.deleteAuto(id);
    }

    @GetMapping("/auto/marca/{marca}")
    public List<Auto> getAutoByMarca(@PathVariable String marca) {
        return autoService.trovaAutoPerMarca(marca);
    }

    @GetMapping("/auto/modello/{modello}")
    @PreAuthorize("hasAnyAuthority('AMMINISTRATORE', 'CLIENTE')")
    public List<Auto> getAutoByModello(@PathVariable String modello) {
        return autoService.trovaAutoPerModello(modello);
    }

    @GetMapping("/auto/anno/{anno}")
    @PreAuthorize("hasAnyAuthority('AMMINISTRATORE', 'CLIENTE')")
    public List<Auto> getAutoByAnno(@PathVariable int anno) {
        return autoService.trovaAutoPerAnno(anno);
    }

    @GetMapping("/auto/intervalloprezzo/{prezzoMin}/{prezzoMax}")
    @PreAuthorize("hasAnyAuthority('AMMINISTRATORE', 'CLIENTE')")
    public List<Auto> getAutoByIntervalloPrezzo(@PathVariable double prezzoMin, @PathVariable double prezzoMax) {
        return autoService.trovaAutoPerIntervalloPrezzo(prezzoMin, prezzoMax);
    }

    @GetMapping("/auto/chilometraggio/{chilometraggio}")
    @PreAuthorize("hasAnyAuthority('AMMINISTRATORE', 'CLIENTE')")
    public List<Auto> getAutoByChilometraggio(@PathVariable int chilometraggio) {
        return autoService.trovaAutoPerChilometraggio(chilometraggio);
    }

    @GetMapping("/auto/colorecarrozzeria/{opzioneColore}")
    @PreAuthorize("hasAnyAuthority('AMMINISTRATORE', 'CLIENTE')")
    public List<Auto> getAutoByOpzioneColore(@PathVariable String opzioneColore) {
        return autoService.trovaAutoPerOpzioneColore(opzioneColore);
    }

    @GetMapping("/auto/tipomotore/{tipoMotore}")
    @PreAuthorize("hasAnyAuthority('AMMINISTRATORE', 'CLIENTE')")
    public List<Auto> getAutoByTipoMotore(@PathVariable String tipoMotore) {
        return autoService.trovaAutoPerTipoMotore(tipoMotore);
    }

    @GetMapping("/auto/intervallotopspeed/{minSpeed}/{maxSpeed}")
    @PreAuthorize("hasAnyAuthority('AMMINISTRATORE', 'CLIENTE')")
    public List<Auto> getAutoByTopSpeed(@PathVariable String minSpeed, @PathVariable String maxSpeed) {
        return autoService.trovaAutoPerTopSpeed(minSpeed, maxSpeed);
    }

    @GetMapping("/auto/intervallocavalli/{minCavalli}/{maxCavalli}")
    @PreAuthorize("hasAnyAuthority('AMMINISTRATORE', 'CLIENTE')")
    public List<Auto> getAutoByCavalli(@PathVariable int minCavalli, @PathVariable int maxCavalli) {
        return autoService.trovaAutoPerCavalli(minCavalli, maxCavalli);
    }

    @GetMapping("/auto/assistente/{driverAssistance}")
    @PreAuthorize("hasAnyAuthority('AMMINISTRATORE', 'CLIENTE')")
    public List<Auto> getAutoByDriverAssistance(@PathVariable String driverAssistance) {
        return autoService.trovaAutoPerDriverAssistance(driverAssistance);
    }

    @GetMapping("/auto/sicurezza/{autoPerSicurezza}")
    @PreAuthorize("hasAnyAuthority('AMMINISTRATORE', 'CLIENTE')")
    public List<Auto> getAutoBySicurezza(@PathVariable String autoPerSicurezza) {
        return autoService.trovaAutoPerSicurezza(autoPerSicurezza);
    }

    @GetMapping("/auto/posti/{posti}")
    @PreAuthorize("hasAnyAuthority('AMMINISTRATORE', 'CLIENTE')")
    public List<Auto> getAutoByPosti(@PathVariable int posti) {
        return autoService.trovaAutoPerPosti(posti);
    }

    @GetMapping("/auto/tappezzeria/{tappezzeria}")
    @PreAuthorize("hasAnyAuthority('AMMINISTRATORE', 'CLIENTE')")
    public List<Auto> getAutoByTappezzeria(@PathVariable String tappezzeria) {
        return autoService.trovaAutoPerTappezzeria(tappezzeria);
    }

    @GetMapping("/auto/caratteristicheinterni/{caratteristicaInterni}")
    @PreAuthorize("hasAnyAuthority('AMMINISTRATORE', 'CLIENTE')")
    public List<Auto> getAutoByFeatureInterni(@PathVariable String caratteristicaInterni) {
        return autoService.trovaAutoPerFeatureInterni(caratteristicaInterni);
    }

    @GetMapping("/auto/caratteristicheesterni/{caratteristicaEsterni}")
    @PreAuthorize("hasAnyAuthority('AMMINISTRATORE', 'CLIENTE')")
    public List<Auto> getAutoByFeatureEsterni(@PathVariable String caratteristicaEsterni) {
        return autoService.trovaAutoPerFeatureEsterni(caratteristicaEsterni);
    }

    @GetMapping("/auto/intervallolunghezza/{minLunghezza}/{maxLunghezza}")
    @PreAuthorize("hasAnyAuthority('AMMINISTRATORE', 'CLIENTE')")
    public List<Auto> getAutoByLunghezza(@PathVariable String minLunghezza, @PathVariable String maxLunghezza) {
        return autoService.trovaAutoPerLunghezza(minLunghezza, maxLunghezza);
    }

    @GetMapping("/auto/intervallolarghezza/{minLarghezza}/{maxLarghezza}")
    @PreAuthorize("hasAnyAuthority('AMMINISTRATORE', 'CLIENTE')")
    public List<Auto> getAutoByLarghezza(@PathVariable String minLarghezza, @PathVariable String maxLarghezza) {
        return autoService.trovaAutoPerLarghezza(minLarghezza, maxLarghezza);
    }

    @GetMapping("/auto/marcaemodello/{marca}/{modello}")
    public List<Auto> getAutoByMarcaEModello(@PathVariable String marca, @PathVariable String modello) {
        return autoService.trovaPerMarcaEModello(marca, modello);
    }

    @GetMapping("/auto/marcamodelloeanno/{marca}/{modello}/{anno}")
    public List<Auto> getAutoByMarcaEModelloEAnno(@PathVariable String marca, @PathVariable String modello, @PathVariable int anno) {
        return autoService.trovaPerMarcaEModelloEAnno(marca, modello, anno);
    }

}
