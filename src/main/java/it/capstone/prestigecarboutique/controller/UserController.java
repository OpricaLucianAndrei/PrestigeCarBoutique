package it.capstone.prestigecarboutique.controller;

import it.capstone.prestigecarboutique.dto.UserDto;
import it.capstone.prestigecarboutique.entity.User;
import it.capstone.prestigecarboutique.exception.BadRequestException;
import it.capstone.prestigecarboutique.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

@RestController
@RequestMapping("/api/prestigecarboutique")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/currentuser")
    public User getProfile( User currentUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        } else {
            // Gestire il caso in cui l'utente non sia autenticato o non sia un'istanza di User
            throw new IllegalStateException("User not authenticated or not found");
        }
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('AMMINISTRATORE')")
    public Page<User> getUsers(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "id") String sortBy){
        return userService.getUserConPaginazione(page,size,sortBy);
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAuthority('AMMINISTRATORE')")
    public Optional<User> getUser(@PathVariable int id){
        return userService.getUserById(id);
    }



    @PutMapping("/users/{id}")
    @PreAuthorize("hasAuthority('AMMINISTRATORE')")
    public User updateUser(@PathVariable int id, @RequestBody @Validated UserDto userDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage()).reduce("", (s, s2) -> s + s2));
        }
        return userService.updateUser(userDto, id);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasAuthority('AMMINISTRATORE')")
    public String deleteUser( @PathVariable int id){
        return userService.deleteUser(id);
    }


}

