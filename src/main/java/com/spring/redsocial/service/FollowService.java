package com.spring.redsocial.service;

import com.spring.redsocial.exception.RedSocialExceptionHandler;
import com.spring.redsocial.model.Follow;
import com.spring.redsocial.model.User;
import com.spring.redsocial.repository.FollowRepository;
import com.spring.redsocial.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    public ResponseEntity<?> follow(String followed_id){
        User followedUser = null;
        try{
            followedUser = userRepository.findById(Integer.valueOf(followed_id)).get();
        }catch(Exception e){
            throw new RedSocialExceptionHandler("El usuario no existe o parámetro invalido");
        }
        User currentUser = authService.getCurrentUser();

        Optional<Follow> isAlreadyFollow = followRepository.isAlreadyFollow(currentUser.getId(), Integer.valueOf(followed_id));
        if( isAlreadyFollow.isPresent() ){
            System.out.println("ya estas siguiendo");
            throw new RedSocialExceptionHandler("Ya estas siguiendo a " + isAlreadyFollow.get().getFollow().getNick());
        }

        Follow follow = new Follow();
        follow.setUser(currentUser);
        follow.setFollow(followedUser);

        Follow followSaved = followRepository.save(follow);

        return new ResponseEntity<>( "Ha comenzado a seguir a " + followSaved.getFollow().getNick(), HttpStatus.OK );
    }

    public void unfollow(){

    }

}
