package it.capstone.prestigecarboutique.service;

import it.capstone.prestigecarboutique.entity.User;
import it.capstone.prestigecarboutique.dto.UserLoginDto;
import it.capstone.prestigecarboutique.exception.UnauthorizedException;
import it.capstone.prestigecarboutique.security.AuthenticationResponse;
import it.capstone.prestigecarboutique.security.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTool jwtTool;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public AuthenticationResponse authenticateUserAndCreateToken(UserLoginDto userLoginDTO){
        User user = userService.getUserByEmail(userLoginDTO.getEmail());

        if (passwordEncoder.matches(userLoginDTO.getPassword() ,user.getPassword())) {

            String token = jwtTool.createToken(user);
            /*String role = user.getRole().toString();
            int id = user.getId();
            String name = jwtTool.createToken(user);
            String surname = user.getRole().toString();
            String email = jwtTool.createToken(user);*/

            return new AuthenticationResponse(token,user);
        }else{
            throw  new UnauthorizedException("Dati errati, controllare i dati inseriti e prova nuovamente, se riscontri ancora problemi contatta l'assistenza di TicketGenius");
        }
    }


}
