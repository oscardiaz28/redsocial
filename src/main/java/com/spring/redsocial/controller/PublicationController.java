package com.spring.redsocial.controller;

import com.spring.redsocial.service.PublicationService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/api/publications")
public class PublicationController {

    private final PublicationService publicationService;

    @PostMapping
    public ResponseEntity<?> save(@RequestParam("text") String texto ){
        if(texto.isBlank()){
            return new ResponseEntity<>("El texto de la publicación es obligatorio", HttpStatus.BAD_REQUEST);
        }
        return publicationService.save(texto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") String id ){
        return publicationService.getOne(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable("id") String id ){
        return publicationService.remove(id);
    }

    // get publications by user
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getByUser(@PathVariable("id") String id,
                                       @RequestParam(value = "page", required = false) String page ){
        return publicationService.getByUser(id, page);
    }

    // upload file
    @PostMapping("/{id}/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file")MultipartFile file, @PathVariable("id") String id ){
        return publicationService.upload(file, id);
    }

    @GetMapping("/media/{file:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable("file") String file ){
        Resource resource = publicationService.getFile(file);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    // FEED
    @GetMapping("/feed")
    public ResponseEntity<?> feed(@RequestParam(value = "page", required = false) String page ){
        return publicationService.feed(page);
    }


}
