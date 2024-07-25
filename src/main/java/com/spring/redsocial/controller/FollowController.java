package com.spring.redsocial.controller;

import com.spring.redsocial.model.Follow;
import com.spring.redsocial.service.FollowService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // dejar de seguir
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> unfollow(@PathVariable("id") String id ){
        return followService.unfollow(id);
    }

    // listado de usuarios a quienes sigue
    @GetMapping("/following")
    public Object getFollowing(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "page", required = false) String page ){
        return followService.following(id, page);
    }

    //listado de followers
    public void getFollowers(){

    }


}
