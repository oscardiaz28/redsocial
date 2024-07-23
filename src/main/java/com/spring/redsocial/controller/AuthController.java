package com.spring.redsocial.controller;

import com.spring.redsocial.dto.AuthResponse;
import com.spring.redsocial.dto.LoginRequest;
import com.spring.redsocial.dto.SignupRequest;
import com.spring.redsocial.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest){
        AuthResponse authResponse = authService.signup(signupRequest);
        return new ResponseEntity<>( authResponse.getMessage(), authResponse.getStatus() );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        AuthResponse authResponse = authService.login(loginRequest);
        return new ResponseEntity<>( authResponse, authResponse.getStatus() );
    }

}
