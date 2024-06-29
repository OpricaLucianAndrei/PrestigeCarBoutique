package it.capstone.prestigecarboutique.service;


import it.capstone.prestigecarboutique.dto.PrenotazioneDto;
import it.capstone.prestigecarboutique.entity.Auto;
import it.capstone.prestigecarboutique.entity.Prenotazione;
import it.capstone.prestigecarboutique.entity.User;
import it.capstone.prestigecarboutique.exception.NotFoundException;
import it.capstone.prestigecarboutique.repository.AutoRepository;
import it.capstone.prestigecarboutique.repository.PrenotazioneRepository;
import it.capstone.prestigecarboutique.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class PrenotazioneService {
    @Autowired
    private PrenotazioneRepository prenotazioneRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AutoRepository autoRepository;
    @Autowired
    private JavaMailSenderImpl javaMailSenderImpl;

    public List<Prenotazione> getTutteLePrenotazioni() {
        return prenotazioneRepository.findAll();
    }

    public Prenotazione getPrenotazioneById(int id) {
        return prenotazioneRepository.findById(id).orElse(null);
    }

    public String creaPrenotazione(PrenotazioneDto prenotazioneDto) {
        // Recupera l'utente dal repository utilizzando l'ID dal DTO
        Optional<User> optionalUser = userRepository.findById(prenotazioneDto.getUserId());

        // Verifica se l'utente esiste
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Recupera l'auto dal repository utilizzando l'ID dal DTO
            Optional<Auto> optionalAuto = autoRepository.findById(prenotazioneDto.getAutoId());

            // Verifica se l'auto esiste
            if (optionalAuto.isPresent()) {
                Auto auto = optionalAuto.get();

                // Verifica la disponibilità dell'auto per la data specificata
                boolean disponibile = verificaDisponibilitaAutoPerDataEora(auto, prenotazioneDto.getDataPrenotazione(), prenotazioneDto.getOraPrenotazione());

                // Se l'auto è disponibile, crea la prenotazione
                if (disponibile) {
                    // Crea un'istanza di Prenotazione e imposta l'utente e l'auto
                    Prenotazione prenotazione = new Prenotazione();
                    prenotazione.setUser(user);
                    prenotazione.setAuto(auto);

                    // Imposta gli altri attributi della prenotazione dal DTO
                    prenotazione.setDataPrenotazione(prenotazioneDto.getDataPrenotazione());
                    prenotazione.setOraPrenotazione(prenotazioneDto.getOraPrenotazione());
                    // Imposta altri attributi della prenotazione, se necessario

                    // Salva la prenotazione nel repository
                    prenotazioneRepository.save(prenotazione);
                    sendMailPrenotazioneAuto(user.getEmail(), user.getNome(), user.getCognome(), auto, prenotazione.getDataPrenotazione(), prenotazione.getOraPrenotazione());
                    return "Prenotazione salvata con successo";
                } else {
                    // Gestisci la situazione in cui l'auto non è disponibile, ad esempio, lanciando un'eccezione
                    throw new NotFoundException("L'auto non è disponibile per la data specificata.");
                }
            } else {
                // Gestisci la situazione in cui l'auto con l'ID specificato non è stata trovata, ad esempio, lanciando un'eccezione
                throw new NotFoundException("Auto non trovata con l'ID specificato.");
            }
        } else {
            // Gestisci la situazione in cui l'utente con l'ID specificato non è stato trovato, ad esempio, lanciando un'eccezione
            throw new NotFoundException("Utente non trovato con l'ID specificato.");
        }
    }

    private boolean verificaDisponibilitaAutoPerDataEora(Auto auto, LocalDate data, LocalTime ora) {
        // Combina LocalDate e LocalTime in un LocalDateTime per ottenere la data e l'ora complete
        LocalDateTime dateTime = LocalDateTime.of(data, ora);

        // Implementa la logica per verificare la disponibilità dell'auto per la data e l'ora specificate
        // Puoi, ad esempio, controllare se ci sono altre prenotazioni per l'auto che si sovrappongono
        // con l'intervallo di tempo specificato e restituire true se non ce ne sono, altrimenti restituire false
        // Questa è solo una implementazione di esempio, adatta la logica secondo le tue esigenze

        // Supponiamo che auto.getPrenotazioni() restituisca tutte le prenotazioni dell'auto
        return auto.getPrenotazioni().stream()
                .noneMatch(prenotazione -> {
                    LocalDateTime prenotazioneInizio = prenotazione.getDataPrenotazione().atTime(prenotazione.getOraPrenotazione());
                    LocalDateTime prenotazioneFine = prenotazioneInizio.plus(Duration.ofHours(1)); // Supponendo che ogni prenotazione duri un'ora
                    LocalDateTime intervalloInizio = dateTime;
                    LocalDateTime intervalloFine = intervalloInizio.plus(Duration.ofHours(1)); // Intervallo di un'ora per la prenotazione
                    return intervalloInizio.isBefore(prenotazioneFine) && intervalloFine.isAfter(prenotazioneInizio);
                });
    }

    public List<Prenotazione> getPrenotazioni(){
        return prenotazioneRepository.findAll();
    }

    public Prenotazione updatePrenotazione(int id, PrenotazioneDto prenotazioneDto) {
        // Controlla se la prenotazione esiste
        Prenotazione prenotazioneEsistente = prenotazioneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prenotazione non trovata con l'ID specificato."));

        // Aggiorna l'utente e l'auto solo se sono stati forniti nel DTO
        prenotazioneEsistente.setUser(userRepository.findById(prenotazioneDto.getUserId())
                    .orElseThrow(() -> new NotFoundException("Utente non trovato con l'ID specificato.")));

        prenotazioneEsistente.setAuto(autoRepository.findById(prenotazioneDto.getAutoId())
                    .orElseThrow(() -> new NotFoundException("Auto non trovata con l'ID specificato.")));


        // Aggiorna gli altri attributi della prenotazione se sono stati forniti nel DTO
        prenotazioneEsistente.setDataPrenotazione(prenotazioneDto.getDataPrenotazione());
        prenotazioneEsistente.setOraPrenotazione(prenotazioneDto.getOraPrenotazione());


        // Salva e restituisce la prenotazione aggiornata
        return prenotazioneRepository.save(prenotazioneEsistente);
    }

    public String deletePrenotazione(int id) {
        // Controlla se la prenotazione esiste
        Prenotazione prenotazioneEsistente = prenotazioneRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Prenotazione non trovata con l'ID specificato."));

        // Elimina la prenotazione
        prenotazioneRepository.delete(prenotazioneEsistente);
        return "Prenotazione eliminata con successo";
    }


    private void sendMailPrenotazioneAuto(String email, String nome, String cognome, Auto auto, LocalDate dataTestDrive, LocalTime oraTestDrive) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Prenotazione test drive presso Prestige Car Boutique effettuata con successo!");
        message.setText("Gentile " + cognome + " " + nome + ",\n" +
                "\n" +
                "Siamo lieti di confermare la tua prenotazione per un test drive presso il nostro showroom Prestige Car Boutique.\n" +
                "\n" +
                "Dettagli della prenotazione:\n" +
                "Auto: " + auto.getMarca() + " " + auto.getModello() + " del " + auto.getAnno() + "\n" +
                "Data e Ora del Test Drive: " + dataTestDrive + " alle " + oraTestDrive + "\n" +
                "\n" +
                "Ti aspettiamo presso la nostra sede in Via Roma, 1, ROMA RM. Il nostro team sarà a tua disposizione per qualsiasi necessità e per fornirti tutte le informazioni riguardanti l'auto.\n" +
                "\n" +
                "Se hai bisogno di ulteriori dettagli o desideri modificare l'appuntamento, non esitare a contattarci via email all'indirizzo concessionariopcb@gmail.com o telefonicamente al numero 06 54321.\n" +
                "\n" +
                "Ti ringraziamo per aver scelto Prestige Car Boutique e non vediamo l'ora di darti il benvenuto nel nostro showroom.\n" +
                "\n" +
                "Cordiali saluti,\n" +
                "\n" +
                "Prestige Car Boutique\n" +
                "Via Roma, 1, ROMA RM\n" +
                "concessionariopcb@gmail.com\n" +
                "06 54321\n");


        javaMailSenderImpl.send(message);
    }
}
