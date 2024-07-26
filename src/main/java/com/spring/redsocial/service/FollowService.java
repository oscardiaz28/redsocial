package com.spring.redsocial.service;

import com.spring.redsocial.exception.RedSocialExceptionHandler;
import com.spring.redsocial.model.Follow;
import com.spring.redsocial.model.User;
import com.spring.redsocial.repository.FollowRepository;
import com.spring.redsocial.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public ResponseEntity<?> unfollow(String id){
        Optional<Follow> follow = followRepository.findById(Integer.valueOf(id));
        if(follow.isPresent()){
            followRepository.deleteById(Integer.valueOf(id));
            return new ResponseEntity<>("Se ha dejado de seguir a " + follow.get().getFollow().getNick(),
                    HttpStatus.OK );
        }
        return new ResponseEntity<>("Error en la consulta", HttpStatus.BAD_REQUEST);
    }

    public Object following(String id, String page){
        Integer defaultPage = 0;
        User currentUser = authService.getCurrentUser();
        if( id != null ) {
            currentUser = userRepository.findById(Integer.valueOf(id)).orElseThrow(
                    () -> new RedSocialExceptionHandler("No se ha encontrado el usuario") ) ;
        }
        if( page != null && Integer.parseInt(page) > 0 ){
            defaultPage = Integer.parseInt(page);
        }
        Pageable pageable = PageRequest.of(defaultPage, 10);

        List<Follow> following = followRepository.getFollowing(currentUser.getId());
        Page<Follow> paging = getPage(following, pageable);

        Map<String, Object> result = new HashMap<>();
        result.put("totalElements", paging.getTotalElements());
        result.put("totalPages", paging.getTotalPages());
        result.put("currentPage", paging.getNumber() + 1);
        result.put("itemsPerPage", paging.getSize());
        result.put("content", paging.getContent());

        if( id != null){
            Integer loginId = authService.getCurrentUser().getId();

            List<Follow> getUserFollowing = followRepository.getUserFollowing(loginId,
                    Integer.parseInt(id));
            List<Follow> getUserFollowers = followRepository.getUserFollowers(loginId, Integer.parseInt(id));

            List<Integer> userFollowing = getUserFollowing.stream()
                    .map( follow -> follow.getFollow().getId()  )
                    .toList();

            List<Integer> userFollowers = getUserFollowers.stream()
                    .map( follow -> follow.getUser().getId()  )
                    .toList();

            result.put("user_following", userFollowing);
            result.put("user_followers", userFollowers);
        }
        // TODO: AGREGAR LOS USUARIOS QUE SIGO COMO USUARIO LOGUEADO, Y LOS QUE ME SIGUEN
        // TODO ESTO DEL ARREGLO ANTERIOR.

        return result;
    }

    private Page<Follow> getPage(List<Follow> follows, Pageable pageable){
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        int offset = pageNumber * pageSize;

        int total = follows.size();
        List<Follow> pagedList = follows.stream()
                .skip(offset)
                .limit(pageSize)
                .toList();
        /**
         * Implementacion de la interfaz Page, permite representar una página de resultados.
         */
        return new PageImpl<>(pagedList, pageable, total);
    }

    public List<Follow> followers(String id){
        User currentUser = authService.getCurrentUser();
        if( id != null ) currentUser = userRepository.findById(Integer.valueOf(id)).orElseThrow(
                () -> new RedSocialExceptionHandler("No se ha encontrado el usuario") ) ;
        return followRepository.getFollowers(currentUser.getId());
    }

}
