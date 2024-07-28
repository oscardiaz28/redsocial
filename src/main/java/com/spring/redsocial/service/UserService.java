package com.spring.redsocial.service;

import com.spring.redsocial.dto.AuthResponse;
import com.spring.redsocial.dto.SignupRequest;
import com.spring.redsocial.exception.RedSocialExceptionHandler;
import com.spring.redsocial.file.FileUploadService;
import com.spring.redsocial.file.FileUploadServiceImpl;
import com.spring.redsocial.mapper.UserMapper;
import com.spring.redsocial.model.Follow;
import com.spring.redsocial.model.User;
import com.spring.redsocial.repository.FollowRepository;
import com.spring.redsocial.repository.PublicationRepository;
import com.spring.redsocial.repository.UserRepository;
import com.spring.redsocial.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.*;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final FollowRepository followRepository;
    private final PublicationRepository publicationRepository;

    public AuthResponse save(SignupRequest signupRequest){
        Map<String, String> errors = new HashMap<>();
        Optional<User> optionalEmail = userRepository.findByEmail(signupRequest.getEmail());
        Optional<User> optionalNick = userRepository.findByNick(signupRequest.getNick());

        if( optionalEmail.isPresent() ){
            errors.put("email", "El email ya existe");
        }
        if(optionalNick.isPresent()){
            errors.put("nick", "El nick ya esta en uso");
        }
        if( !errors.isEmpty() ){
            return AuthResponse.builder()
                    .message(errors)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        User user = userMapper.map(signupRequest);
        userRepository.save(user);
        return AuthResponse.builder()
                .message("Usuario creado satisfactoriamente")
                .status(HttpStatus.CREATED)
                .build();
    }

    public ResponseEntity<?> getProfile(String id){
        Optional<User> userOptional = userRepository.findById(Integer.valueOf(id));
        if( userOptional.isEmpty() ){
            return new ResponseEntity<>( "El usuario no existe o hay un error", HttpStatus.NOT_FOUND );
        }
        return new ResponseEntity<>( userOptional.get(), HttpStatus.OK );
    }


    public Object all(String page){
        User currentUser = getCurrentUser();

        Map<String, Object> response = new HashMap<>();

        if( page != null ){
            //the index start in 0
            Pageable pageable = PageRequest.of(Integer.parseInt(page) - 1, 3);
            Page<User> users = userRepository.findAll(pageable);

            response.put("totalElements", users.getTotalElements());
            response.put("totalPages", users.getTotalPages());
            response.put("currentPage", users.getNumber() + 1);
            response.put("itemsPerPage", users.getSize());
            response.put("content", users.getContent());
            return response;
        }

        List<Follow> getUserFollowing = followRepository.getUserFollowingFromList(currentUser.getId());

        //List<Follow> getUserFollowers = followRepository.getUserFollowers(loginId, Integer.parseInt(id));

        List<Integer> userFollowing = getUserFollowing.stream()
                .map( follow -> follow.getFollow().getId()  )
                .toList();

        /*
        List<Integer> userFollowers = getUserFollowers.stream()
                .map( follow -> follow.getUser().getId()  )
                .toList();
         */
        response.put("content", userRepository.findAll());
        response.put("user_following", userFollowing);
        return response;
    }

    public AuthResponse update(User user){
        User currentUser = getCurrentUser();
        List<User> listado = userRepository.findByEmailOrNick(user.getEmail(), user.getNick());
        if( !listado.isEmpty() && currentUser.getId() != listado.get(0).getId() ){
            throw new RedSocialExceptionHandler("El email o nick ya existe");
        }
        User userUpdated = reflection(currentUser, user);
        return AuthResponse.builder()
                .status(HttpStatus.OK)
                .message("Usuario actualizado correctamente")
                .data(userRepository.save(userUpdated))
                .build();
    }

    public ResponseEntity<?> upload(MultipartFile file){
        User currentUser = this.getCurrentUser();
        String location = "src/main/resources/static/avatars";
        FileUploadService fileUploadService = new FileUploadServiceImpl(location);

        String image  = fileUploadService.almacenarArchivo(file);
        String path = fileUploadService.cargarArchivo(image).toString();

        Map<String, String> response = new HashMap<>();
        response.put("fileName", image);
        response.put("path", path);
        response.put("message", "Imagen de perfil actualizada correctamente");

        if( image != null ){
            currentUser.setImage(image);
            userRepository.save(currentUser);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        return new ResponseEntity<>("Error al subir el archivo", HttpStatus.OK);
    }

    public Resource getAvatar(String nombre){
        String location = "src/main/resources/static/avatars";
        FileUploadService fileUploadService = new FileUploadServiceImpl(location);
        return fileUploadService.cargarComoRecurso(nombre);
    }

    public ResponseEntity<?> counter(String id){
        User currentUser = this.getCurrentUser();
        try{
            if( id != null ) {
                currentUser = userRepository.findById(Integer.parseInt(id)).orElse(null);
            }
        }catch(NumberFormatException e){
            return new ResponseEntity<>("ID invalido", HttpStatus.BAD_REQUEST);
        }
        if( currentUser == null) return new ResponseEntity<>("El usuario no existe", HttpStatus.BAD_REQUEST);

        Map<String, Object> result = new HashMap<>();
        Long following = followRepository.countByUserId(currentUser.getId());
        Long publications = publicationRepository.countByUserId(currentUser.getId());
        Long followers = followRepository.countByFollowId(currentUser.getId());
        result.put("following", following);
        result.put("followers", followers);
        result.put("publications", publications);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private User reflection(User currentUser, User userRequest){
        Field[] fields = userRequest.getClass().getDeclaredFields();
        for( Field field : fields ){
            try {
                field.setAccessible(true);
                Object newValue = field.get(userRequest);
                if( newValue != null){
                    if("password".equals(field.getName())){
                        field.set(currentUser, passwordEncoder.encode( (String) newValue) );
                    }else{
                        field.set(currentUser, newValue);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return currentUser;
    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getPrincipal().toString()).orElseThrow(
                () -> new RedSocialExceptionHandler("User not found") );
    }


}
