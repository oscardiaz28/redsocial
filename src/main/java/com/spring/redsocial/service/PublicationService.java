package com.spring.redsocial.service;

import com.spring.redsocial.model.Publication;
import com.spring.redsocial.model.User;
import com.spring.redsocial.repository.PublicationRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class PublicationService {

    private final PublicationRepository publicationRepository;
    private final AuthService authService;

    public ResponseEntity<?> save(String texto){
        User currentUser = authService.getCurrentUser();
        Publication publication = new Publication();
        publication.setText(texto);
        publication.setUser(currentUser);

        Publication publicationSaved = publicationRepository.save(publication);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Publicacion guardada exitosamenete");
        response.put("data", publicationSaved);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
