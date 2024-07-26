package com.spring.redsocial.controller;

import com.spring.redsocial.service.PublicationService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/publications")
public class PublicationController {

    private PublicationService publicationService;

    @PostMapping
    public ResponseEntity<?> save(@RequestParam("text") String texto ){
        if(texto.isBlank()){
            return new ResponseEntity<>("El texto de la publicación es obligatorio", HttpStatus.BAD_REQUEST);
        }
        return publicationService.save(texto);
    }

    @GetMapping("/{id}")
    public void getOne(@PathVariable("id") String id ){

    }

    @GetMapping
    public void getAll(){

    }


}
