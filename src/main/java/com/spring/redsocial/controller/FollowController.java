package com.spring.redsocial.controller;

import com.spring.redsocial.service.FollowService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/follows")
public class FollowController {

    private final FollowService followService;

    // accion de seguir
    @PostMapping("/save")
    public ResponseEntity<?> follow(@RequestParam("followed") String followed){
        return followService.follow(followed);
    }

    // dejar de seeguir
    public void unfollow(){

    }

    //listado de followers
    public void getFollowers(){

    }

    // listado de usuarios a quien sigo
    public void getFollowed(){

    }

}
