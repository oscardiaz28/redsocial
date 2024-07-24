package com.spring.redsocial.controller;

import com.spring.redsocial.dto.AuthResponse;
import com.spring.redsocial.model.User;
import com.spring.redsocial.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getUsers(
            @RequestParam(required = false) String page
    ){
        return new ResponseEntity<>(userService.all(page), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody User user){
        AuthResponse authResponse = userService.update(user);
        return new ResponseEntity<>( authResponse , HttpStatus.OK );
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<?> profile(@PathVariable("id") String id ){
        return userService.getProfile(id);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("perfil") MultipartFile file){
        return userService.upload(file);
    }

    @GetMapping("/avatar/{file:.+}")
    public ResponseEntity<?> getAvatar(@PathVariable String file){
        Resource resource = userService.getAvatar(file);
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(resource);
    }

}
