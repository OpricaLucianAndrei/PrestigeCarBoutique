package it.capstone.prestigecarboutique.controller;


import it.capstone.prestigecarboutique.dto.UserDto;
import it.capstone.prestigecarboutique.dto.UserLoginDto;
import it.capstone.prestigecarboutique.exception.BadRequestException;
import it.capstone.prestigecarboutique.security.AuthenticationResponse;
import it.capstone.prestigecarboutique.service.AuthService;
import it.capstone.prestigecarboutique.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;
    @PostMapping("/signup")
    @CrossOrigin(origins = "*")
    public String signup(@RequestBody @Validated UserDto userDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new BadRequestException(bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).reduce("",(s, s2) -> s+s2 ));
        }

        return userService.saveUser(userDTO);
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "*")
    public AuthenticationResponse login(@RequestBody @Validated UserLoginDto userLoginDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new BadRequestException(bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).reduce("",(s, s2) -> s+s2 ));
        }

        return authService.authenticateUserAndCreateToken(userLoginDTO);
    }
}

