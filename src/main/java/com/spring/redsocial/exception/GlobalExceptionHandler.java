package com.spring.redsocial.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RedSocialExceptionHandler.class)
    public ResponseEntity<?>handler(RedSocialExceptionHandler e){
        String message = e.getMessage();
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

}
