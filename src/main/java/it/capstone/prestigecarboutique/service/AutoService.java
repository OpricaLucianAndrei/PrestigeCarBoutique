package it.capstone.prestigecarboutique.service;


import com.cloudinary.Cloudinary;
import it.capstone.prestigecarboutique.dto.*;
import it.capstone.prestigecarboutique.entity.*;
import it.capstone.prestigecarboutique.enums.StatoVeicolo;
import it.capstone.prestigecarboutique.exception.NotFoundException;
import it.capstone.prestigecarboutique.repository.*;
import it.capstone.prestigecarboutique.dto.AutoDto;
import it.capstone.prestigecarboutique.dto.OptionalFeaturesDto;
import it.capstone.prestigecarboutique.dto.SpecificheDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AutoService {
    @Autowired
    private AutoRepository autoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AirbagsRepository airbagRepository;
    @Autowired
    private ConsumoCarburanteRepository consumoCarburanteRepository;
    @Autowired
    private DimensioniRepository dimensioniRepository;
    @Autowired
    private EsterniRepository esterniRepository;
    @Autowired
    private ImmaginiRepository immaginiRepository;
    @Autowired
    private InterniRepository interniRepository;
    @Autowired
    private MotoreRepository motoreRepository;
    @Autowired
    private OptionalFeaturesRepository optionalFeaturesRepository;
    @Autowired
    private PerformanceRepository performanceRepository;
    @Autowired
    private PrezzoRepository prezzoRepository;
    @Autowired
    private SicurezzaRepository sicurezzaRepository;
    @Autowired
    private SpecificheRepository specificheRepository;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private JavaMailSenderImpl javaMailSenderImpl;

    public String saveAuto(AutoDto autoDto) {
        Auto auto = new Auto();
        auto.setMarca(autoDto.getMarca());
        auto.setModello(autoDto.getModello());
        auto.setAnno(autoDto.getAnno());
        auto.setChilometraggio(autoDto.getChilometraggio());
        auto.setDescrizione(autoDto.getDescrizione());
        auto.setDisponibilità(autoDto.isDisponibilità());
        auto.setStatoVeicolo(autoDto.getStatoVeicolo());

        // Map Specifiche
        auto.setSpecifiche(mapSpecificheDtoToEntity(autoDto.getSpecifiche()));

        // Map Prezzo
        Prezzo prezzo = new Prezzo();
        prezzo.setPrezzoBase(autoDto.getPrezzo().getPrezzoBase());
        prezzo.setTasse(autoDto.getPrezzo().getTasse());

        // Calculate total price of optional features
        double prezzoOpzioni = autoDto.getOptionalFeatures().stream()
                .mapToDouble(OptionalFeaturesDto::getPrezzo)
                .sum();
        prezzo.setOptional(prezzoOpzioni);

        // Calculate total price
        double prezzoTotale = autoDto.getPrezzo().getPrezzoBase() + autoDto.getPrezzo().getTasse() + prezzoOpzioni;
        prezzo.setPrezzoTotale(prezzoTotale);

        auto.setPrezzo(prezzo);

        // Map OptionalFeatures
        List<OptionalFeatures> optionalFeatures = autoDto.getOptionalFeatures().stream()
                .map(this::mapOptionalFeaturesDtoToEntity)
                .collect(Collectors.toList());
        auto.setOptionalFeatures(optionalFeatures);
        List<Immagini> immagini = autoDto.getImmagini().stream().map(this::mapImmaginiDtoToEntity).collect(Collectors.toList());
        auto.setImmagini(immagini);
        autoRepository.save(auto);
        sendMailToAllUsers(auto);
        return "Auto salvata correttamente";
    }

    private Specifiche mapSpecificheDtoToEntity(SpecificheDto specificheDto) {
        Specifiche specifiche = new Specifiche();

        Performance performance = new Performance();
        performance.setTopSpeed(specificheDto.getPerformance().getTopSpeed());
        performance.setAccelerazione0a100Kmh(specificheDto.getPerformance().getAccelerazione0a100Kmh());

        Motore motore = new Motore();
        motore.setTipo(specificheDto.getPerformance().getMotore().getTipo());
        motore.setCilindrata(specificheDto.getPerformance().getMotore().getCilindrata());
        motore.setCavalli(specificheDto.getPerformance().getMotore().getCavalli());
        motore.setCoppia(specificheDto.getPerformance().getMotore().getCoppia());
        motore.setTrasmissione(specificheDto.getPerformance().getMotore().getTrasmissione());
        motore.setTrazione(specificheDto.getPerformance().getMotore().getTrazione());
        performance.setMotore(motore);

        ConsumoCarburante consumoCarburante = new ConsumoCarburante();
        consumoCarburante.setCittà(specificheDto.getPerformance().getConsumoCarburante().getCittà());
        consumoCarburante.setAutostrada(specificheDto.getPerformance().getConsumoCarburante().getAutostrada());
        consumoCarburante.setCombinato(specificheDto.getPerformance().getConsumoCarburante().getCombinato());
        performance.setConsumoCarburante(consumoCarburante);

        specifiche.setPerformance(performance);

        Dimensioni dimensioni = new Dimensioni();
        dimensioni.setLunghezza(specificheDto.getDimensioni().getLunghezza());
        dimensioni.setLarghezza(specificheDto.getDimensioni().getLarghezza());
        dimensioni.setAltezza(specificheDto.getDimensioni().getAltezza());
        dimensioni.setPasso(specificheDto.getDimensioni().getPasso());
        dimensioni.setPesoAVuoto(specificheDto.getDimensioni().getPesoAVuoto());
        dimensioni.setCapacitaSerbatoio(specificheDto.getDimensioni().getCapacitaSerbatoio());
        specifiche.setDimensioni(dimensioni);

        Interni interni = new Interni();
        interni.setPosti(specificheDto.getInterni().getPosti());
        interni.setTappezzeria(specificheDto.getInterni().getTappezzeria());
        interni.setCaratteristiche(specificheDto.getInterni().getCaratteristiche());
        specifiche.setInterni(interni);

        Esterni esterni = new Esterni();
        esterni.setOpzioniColori(specificheDto.getEsterni().getOpzioniColori());
        esterni.setCaratteristiche(specificheDto.getEsterni().getCaratteristiche());
        specifiche.setEsterni(esterni);

        Sicurezza sicurezza = new Sicurezza();
        sicurezza.setAssistenzaAllaGuida(specificheDto.getSicurezza().getAssistenzaAllaGuida());
        sicurezza.setSicurezza(specificheDto.getSicurezza().getSicurezza());

        Airbags airbags = new Airbags();
        airbags.setAnteriore(specificheDto.getSicurezza().getAirbags().isAnteriore());
        airbags.setLaterale(specificheDto.getSicurezza().getAirbags().isLaterale());
        airbags.setTendina(specificheDto.getSicurezza().getAirbags().isTendina());
        airbags.setGinocchia(specificheDto.getSicurezza().getAirbags().isGinocchia());
        sicurezza.setAirbags(airbags);

        specifiche.setSicurezza(sicurezza);

        return specifiche;
    }

    private OptionalFeatures mapOptionalFeaturesDtoToEntity(OptionalFeaturesDto optionalFeatureDto) {
        OptionalFeatures optionalFeature = new OptionalFeatures();
        optionalFeature.setNome(optionalFeatureDto.getNome());
        optionalFeature.setPrezzo(optionalFeatureDto.getPrezzo());
        return optionalFeature;
    }
    private Immagini mapImmaginiDtoToEntity(ImmaginiDto immaginiDto){
        Immagini immagini = new Immagini();
        immagini.setOpzioneColore(immaginiDto.getOpzioneColore());
        immagini.setUrl(immaginiDto.getUrl());
        return immagini;
    }

    public String updateAuto(int id, AutoDto autoDto) {
        Optional<Auto> optionalAuto = autoRepository.findById(id);

        if (optionalAuto.isPresent()) {
            Auto auto = optionalAuto.get();
            auto.setMarca(autoDto.getMarca());
            auto.setModello(autoDto.getModello());
            auto.setAnno(autoDto.getAnno());
            auto.setChilometraggio(autoDto.getChilometraggio());
            auto.setDescrizione(autoDto.getDescrizione());
            auto.setDisponibilità(autoDto.isDisponibilità());
            auto.setStatoVeicolo(autoDto.getStatoVeicolo());

            // Map Specifiche
            auto.setSpecifiche(mapSpecificheDtoToEntity(autoDto.getSpecifiche()));

            // Map Prezzo
            Prezzo prezzo = new Prezzo();
            prezzo.setPrezzoBase(autoDto.getPrezzo().getPrezzoBase());
            prezzo.setTasse(autoDto.getPrezzo().getTasse());

            // Calculate total price of optional features
            double prezzoOpzioni = autoDto.getOptionalFeatures().stream()
                    .mapToDouble(OptionalFeaturesDto::getPrezzo)
                    .sum();
            prezzo.setOptional(prezzoOpzioni);

            // Calculate total price
            double prezzoTotale = autoDto.getPrezzo().getPrezzoBase() + autoDto.getPrezzo().getTasse() + prezzoOpzioni;
            prezzo.setPrezzoTotale(prezzoTotale);

            auto.setPrezzo(prezzo);

            // Map OptionalFeatures
            List<OptionalFeatures> optionalFeatures = autoDto.getOptionalFeatures().stream()
                    .map(this::mapOptionalFeaturesDtoToEntity)
                    .collect(Collectors.toList());
            auto.setOptionalFeatures(optionalFeatures);

            // Map Immagini
            List<Immagini> immagini = autoDto.getImmagini().stream()
                    .map(this::mapImmaginiDtoToEntity)
                    .collect(Collectors.toList());
            auto.setImmagini(immagini);

            autoRepository.save(auto);
            sendMailToAllUsersAggiornamentoAuto(auto);
            return "Auto aggiornata con id "+id+" correttamente";
        } else {
            return "Auto non trovata";
        }
    }


    public Optional<Auto> getAutoById(int id) {
        return autoRepository.findById(id);
    }

    public Page<Auto> getAutoConPaginazione(int page, int size, String sortBy, String sortOrder) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortBy));
        return autoRepository.findAll(pageable);
    }
    public List<Auto> trovaTutteLeAuto() {
        return autoRepository.findAll();
    }

    public List<Auto> trovaAutoPerMarca(String marca) {
        return autoRepository.findByMarca(marca);
    }

    public List<Auto> trovaAutoPerModello(String modello) {
        return autoRepository.findByModello(modello);
    }

    public List<Auto> trovaAutoPerAnno(int anno) {
        return autoRepository.findByAnno(anno);
    }

    public List<Auto> trovaAutoPerIntervalloPrezzo(double prezzoMin, double prezzoMax) {
        return autoRepository.findByIntervalloPrezzo(prezzoMin, prezzoMax);
    }

    public List<Auto> trovaAutoPerChilometraggio(int chilometraggio) {
        return autoRepository.findAutoByChilometraggio(chilometraggio);
    }

    public List<Auto> trovaAutoPerOpzioneColore(String opzioneColore) {
        return autoRepository.findBySpecifiche_Esterni_OpzioniColoriContaining(opzioneColore);
    }

    public List<Auto> trovaAutoPerTipoMotore(String tipoMotore) {
        return autoRepository.findBySpecifiche_Performance_Motore_Tipo(tipoMotore);
    }

    public List<Auto> trovaAutoPerTopSpeed(String minSpeed, String maxSpeed) {
        return autoRepository.findBySpecifiche_Performance_TopSpeedBetween(minSpeed, maxSpeed);
    }

    public List<Auto> trovaAutoPerCavalli(int minCavalli, int maxCavalli) {
        return autoRepository.findBySpecifiche_Performance_Motore_CavalliBetween(minCavalli, maxCavalli);
    }

    public List<Auto> trovaAutoPerDriverAssistance(String feature) {
        return autoRepository.findBySpecifiche_Sicurezza_AssistenzaAllaGuidaContaining(feature);
    }

    public List<Auto> trovaAutoPerSicurezza(String feature) {
        return autoRepository.findBySpecifiche_Sicurezza_SicurezzaContaining(feature);
    }

    public List<Auto> trovaAutoPerPosti(int posti) {
        return autoRepository.findBySpecifiche_Interni_Posti(posti);
    }

    public List<Auto> trovaAutoPerTappezzeria(String tappezzeria) {
        return autoRepository.findBySpecifiche_Interni_Tappezzeria(tappezzeria);
    }

    public List<Auto> trovaAutoPerFeatureInterni(String feature) {
        return autoRepository.findBySpecifiche_Interni_CaratteristicheContaining(feature);
    }

    public List<Auto> trovaAutoPerFeatureEsterni(String feature) {
        return autoRepository.findBySpecifiche_Esterni_CaratteristicheContaining(feature);
    }

    public List<Auto> trovaAutoPerLunghezza(String minLunghezza, String maxLunghezza) {
        return autoRepository.findBySpecifiche_Dimensioni_LunghezzaBetween(minLunghezza, maxLunghezza);
    }

    public List<Auto> trovaAutoPerLarghezza(String minLarghezza, String maxLarghezza) {
        return autoRepository.findBySpecifiche_Dimensioni_LarghezzaBetween(minLarghezza, maxLarghezza);
    }

    public List<Auto> trovaPerMarcaEModello(String marca, String modello){
        return autoRepository.findByMarcaAndModello(marca, modello);
    }

    public List<Auto> trovaPerMarcaEModelloEAnno(String marca, String modello, int anno){
        return autoRepository.findByMarcaAndModelloAndAnno(marca, modello, anno);
    }


    public String deleteAuto(int id) {
        Optional<Auto> autoOptional = getAutoById(id);

        if (autoOptional.isPresent()) {
            autoRepository.delete(autoOptional.get());
            return "Auto con id: "+ id + " è stato eliminato";
        } else {
            throw new NotFoundException("Nessun auto trovato con ID: "+id);
        }
    }









    public void sendMailToAllUsers(Auto auto) {
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            sendMailAggiuntaNuovaMacchina(user.getEmail(), user.getNome(), user.getCognome(), auto);
        }
    }

    private void sendMailAggiuntaNuovaMacchina(String email, String nome, String cognome, Auto auto) {
        String colori;
        if (auto.getSpecifiche().getEsterni() != null && auto.getSpecifiche().getEsterni().getOpzioniColori() != null) {
            StringBuilder coloriBuilder = new StringBuilder();
            for (String colore : auto.getSpecifiche().getEsterni().getOpzioniColori()) {
                if (coloriBuilder.length() > 0) {
                    coloriBuilder.append(", ");
                }
                coloriBuilder.append(colore);
            }
            colori = coloriBuilder.toString();
        } else {
            colori = "Colori non disponibili";
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Nuova Auto Disponibile presso Prestige Car Boutique!");
        message.setText("Gentile " + cognome + " " + nome + ",\n" +
                "\n" +
                "Siamo entusiasti di annunciare che una nuova auto è stata aggiunta al nostro inventario presso Prestige Car Boutique!\n" +
                "\n" +
                "La nuova aggiunta è una " + auto.getMarca() + " " + auto.getModello() + " del " + auto.getAnno() + ". Con caratteristiche eccezionali e un design raffinato, siamo sicuri che questa auto catturerà la tua attenzione.\n" +
                "\n" +
                "Ecco alcune delle specifiche principali:\n" +
                "- Chilometraggio: " + auto.getChilometraggio() + " km\n" +
                "- Colori: " + colori + "\n" +
                "- Prezzo: " + auto.getPrezzo().getPrezzoBase() + " EUR\n" +
                "- Descrizione: " + auto.getDescrizione() + "\n" +
                "\n" +
                "Ti invitiamo a visitare il nostro showroom per vedere questa meravigliosa auto di persona e, se desideri, effettuare un test drive. Non esitare a contattarci per ulteriori informazioni o per prenotare un appuntamento. Puoi raggiungerci via email all'indirizzo concessionariopcb@gmail.com o telefonicamente al numero 06 54321.\n" +
                "\n" +
                "Continua a seguirci per rimanere aggiornato su tutte le novità e le offerte speciali che abbiamo in serbo per te.\n" +
                "\n" +
                "Cordiali saluti,\n" +
                "\n" +
                "Prestige Car Boutique\n" +
                "Via Roma, 1, ROMA RM\n" +
                "concessionariopcb@gmail.com\n" +
                "06 54321\n");

        javaMailSenderImpl.send(message);
    }

    public void sendMailToAllUsersAggiornamentoAuto(Auto auto) {
        List<User> userList = userRepository.findAll();
        for (User user : userList) {
            sendMailAggiornamentoMacchina(user.getEmail(), user.getNome(), user.getCognome(), auto);
        }
    }
    private void sendMailAggiornamentoMacchina(String email, String nome, String cognome, Auto auto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Aggiornamento Auto nel nostro inventario Prestige Car Boutique!");
        message.setText("Gentile " + cognome + " " + nome + ",\n" +
                "\n" +
                "Siamo felici di informarti che una delle auto nel nostro inventario Prestige Car Boutique è stata aggiornata con nuove specifiche!\n" +
                "\n" +
                "Si tratta di una " + auto.getMarca() + " " + auto.getModello() + " del " + auto.getAnno() + ". Con caratteristiche eccezionali e un design raffinato, siamo sicuri che questa auto catturerà ancora di più la tua attenzione.\n" +
                "\n" +

                "Ti invitiamo a visitare il nostro showroom per vedere questa meravigliosa auto di persona e, se desideri, effettuare un test drive. Non esitare a contattarci per ulteriori informazioni o per prenotare un appuntamento. Puoi raggiungerci via email all'indirizzo concessionariopcb@gmail.com o telefonicamente al numero 06 54321.\n" +
                "\n" +
                "Continua a seguirci per rimanere aggiornato su tutte le novità e le offerte speciali che abbiamo in serbo per te.\n" +
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
