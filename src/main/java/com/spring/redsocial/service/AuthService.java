package com.spring.redsocial.service;

import com.spring.redsocial.dto.AuthResponse;
import com.spring.redsocial.dto.LoginRequest;
import com.spring.redsocial.dto.SignupRequest;
import com.spring.redsocial.exception.RedSocialExceptionHandler;
import com.spring.redsocial.model.User;
import com.spring.redsocial.repository.UserRepository;
import com.spring.redsocial.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public AuthResponse signup(SignupRequest signupRequest){
        return userService.save(signupRequest);
    }

    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getPrincipal().toString()).
                orElseThrow( () -> new RedSocialExceptionHandler("User not found"));
    }

    public AuthResponse login(LoginRequest loginRequest){
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        Authentication authentication = this.authentication(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.createToken(authentication);

        return AuthResponse.builder()
                .message("user login")
                .status(HttpStatus.OK)
                .token(token)
                .build();
    }

    private Authentication authentication(String email, String password){
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if( !passwordEncoder.matches(password, userDetails.getPassword()) ){
            throw new RedSocialExceptionHandler("Contraseña incorrecta");
        }
        return new UsernamePasswordAuthenticationToken(
            userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities()
        );
    }

}
