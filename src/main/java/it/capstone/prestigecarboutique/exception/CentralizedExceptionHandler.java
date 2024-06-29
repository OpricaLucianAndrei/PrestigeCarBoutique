package it.capstone.prestigecarboutique.exception;


import it.capstone.prestigecarboutique.entity.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CentralizedExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> NotFoundHandler(NotFoundException exception){
        Error error = new Error();
        error.setMessaggio(exception.getMessage());
        error.setDataErrore(LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> UnauthorizedHandler(UnauthorizedException exception){
        Error error = new Error();
        error.setMessaggio(exception.getMessage());
        error.setDataErrore(LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> BadRequestHandler (BadRequestException exception){
        Error error = new Error();
        error.setMessaggio(exception.getMessage());
        error.setDataErrore(LocalDateTime.now());
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }
}
