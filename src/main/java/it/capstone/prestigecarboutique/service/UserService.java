package it.capstone.prestigecarboutique.service;


import com.cloudinary.Cloudinary;
import it.capstone.prestigecarboutique.dto.UserDto;
import it.capstone.prestigecarboutique.entity.User;
import it.capstone.prestigecarboutique.enums.Ruolo;
import it.capstone.prestigecarboutique.exception.BadRequestException;
import it.capstone.prestigecarboutique.exception.NotFoundException;
import it.capstone.prestigecarboutique.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    @Autowired(required = false)
    private UserRepository userRepository;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private JavaMailSenderImpl javaMailSenderImpl;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public User getUserByEmail(String email){
        Optional<User> userOptional =  userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            return userOptional.get();
        }else{
            throw new NotFoundException("User with email: "+email+" not found");
        }
    }

    public Optional<User> getUserById(int id){
        Optional<User> userOptional =  userRepository.findById(id);
        if(userOptional.isPresent()){
            return userRepository.findById(id);
        }
        else{
            throw new NotFoundException("User with id: "+id+" not found");
        }
    }


    public Page<User> getUserConPaginazione(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page,size, Sort.by(sortBy));
        return  userRepository.findAll(pageable);
    }
    public List<User> getUsers(){
        return  userRepository.findAll();
    }



    public String saveUser(UserDto userDto){


        Optional<User> userOptional =  userRepository.findByEmail(userDto.getEmail());

        if(userOptional.isPresent()){
            throw new BadRequestException("Questa email è già associata ad un account!");

        }else{
            User user = new User();
            user.setNome(userDto.getNome());
            user.setCognome(userDto.getCognome());
            user.setTelefono(userDto.getTelefono());
            //non è obbligatorio inserire foto in fase di registrazione
            user.setPictureProfile(userDto.getPictureProfile());

            user.setEmail(userDto.getEmail());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));

            // aggiungo ruolo ceh di default sara USER
            user.setRuolo(Ruolo.CLIENTE);
            userRepository.save(user);
            sendMailCreazioneProfilo(user.getEmail(), user.getNome(), user.getCognome());
            return "Utente aggiunto, id: " + user.getId()+" ,ruolo: "+user.getRuolo();
        }

    }

    public User updateUser(UserDto userUpdate, int id) throws NotFoundException {
        Optional<User> userOpt = getUserById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setNome(userUpdate.getNome());
            user.setCognome(userUpdate.getCognome());
            user.setTelefono(userUpdate.getTelefono());
            user.setPictureProfile(userUpdate.getPictureProfile());
            user.setEmail(userUpdate.getEmail());
            user.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
            return userRepository.save(user);
        } else {
            throw new NotFoundException("Non risulta nessun utente con il seguente id: "+ id);
        }
    }

    public User updateUserParse(int id, Map<String, Object> update) throws NotFoundException {
        Optional<User> userOpt = getUserById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            update.forEach((key, value) -> {
                switch (key) {
                    case "nome":
                        user.setNome((String) value);
                        break;
                    case "cognome":
                        user.setCognome((String) value);
                        break;
                    case "email":
                        user.setEmail((String) value);
                        break;
                    case "telefono":
                        user.setTelefono((String) value);
                        break;
                    case "password":
                        user.setPassword(passwordEncoder.encode((CharSequence) value));
                        sendMailModificaPassword(user.getEmail());
                    default:
                        throw new IllegalArgumentException("Campo non valido: " + key);
                }
            });

            userRepository.save(user);

            return user;
        } else {
            throw new NotFoundException("Nessun utente trovato con id: " + id);
        }
    }



    public String deleteUser(int id) throws NotFoundException {
        Optional<User> userOpt = getUserById(id);

        if (userOpt.isPresent()) {
            userRepository.delete(userOpt.get());
            return "Utente con id: "+ id + " eliminato dal database";
        } else {
            throw new NotFoundException("Nessun dipendente trovato con matricola: "+id);
        }


    }



    public String patchPictureProfileUser(int id, MultipartFile foto) throws NotFoundException, IOException {
        Optional<User> userOptional = getUserById(id);
        if (userOptional.isPresent()){
            String url =(String) cloudinary.uploader().upload(foto.getBytes(), Collections.emptyMap()).get("url");
            User user = userOptional.get();
            user.setPictureProfile(url);
            userRepository.save(user);
            return "Immagine profilo aggiornata!";
        }else{
            throw new NotFoundException("Impossibile impostare immagine del profilo, non è stato trovato nessun utente con matricola: "+id);
        }
    }


private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private void sendMailCreazioneProfilo(String email, String nome, String cognome) {
        try {
            MimeMessage message = javaMailSenderImpl.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject("Benvenuto in Prestige Car Boutique!");

            // HTML message with placeholders and properly escaped percentage signs
            String htmlMessage = """
                <html>
                <body>
                <img src='cid:welcome' style='width:100%%; height:auto;'>
                <h1>Gentile %s %s,</h1>
                <p>Siamo lieti di darti il benvenuto in Prestige Car Boutique!</p>
                <p>Grazie per esserti registrato con noi. Siamo entusiasti di averti come cliente e non vediamo l'ora di aiutarti a trovare l'auto dei tuoi sogni. Il nostro obiettivo è offrirti un'esperienza di acquisto unica e personalizzata, all'altezza delle tue aspettative.</p>
                <p>Non esitare a contattarci per qualsiasi domanda o per prenotare un test drive. Puoi raggiungerci via email all'indirizzo <a href="mailto:concessionariopcb@gmail.com">concessionariopcb@gmail.com</a> o telefonicamente al numero 06 54321. Ti invitiamo anche a visitare il nostro sito web per scoprire tutte le nostre offerte.</p>
                <p>Ancora una volta, benvenuto in Prestige Car Boutique. Siamo entusiasti di iniziare questo viaggio insieme a te e di fornirti il miglior servizio possibile.</p>
                <p>Cordiali saluti,</p>
                <p>Prestige Car Boutique</p>
                <p>Via Roma, 1, ROMA RM</p>
                <p><a href="mailto:concessionariopcb@gmail.com">concessionariopcb@gmail.com</a></p>
                <p>06 54321</p>
                <img src='cid:logo' style='width:120px; height:auto;'>
                </body>
                </html>
                """;

            // Format the message with the user's surname and name
            String formattedMessage = String.format(htmlMessage, cognome, nome);
            helper.setText(formattedMessage, true);

            // Add the inline images
            ClassPathResource welcomeImage = new ClassPathResource("static/images/welcome.png");
            helper.addInline("welcome", welcomeImage);
            ClassPathResource logoImage = new ClassPathResource("static/images/2.png");
            helper.addInline("logo", logoImage);

            // Send the email
            javaMailSenderImpl.send(message);
        } catch (MessagingException e) {
            logger.error("Errore nell'invio della mail a {}", email, e);
        }
    }



    private void sendMailModificaPassword(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Richiesta modifica password");
        message.setText("Attenzione, la tua password è stata modificata, se non sei stato tu a richiederlo, invia una segnalazione!");

        javaMailSenderImpl.send(message);
    }



}