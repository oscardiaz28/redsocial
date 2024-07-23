package com.spring.redsocial.mapper;

import com.spring.redsocial.dto.SignupRequest;
import com.spring.redsocial.model.Role;
import com.spring.redsocial.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User map(SignupRequest signupRequest){
        User user = new User();
        user.setName(signupRequest.getName());
        user.setNick(signupRequest.getNick());
        user.setEmail(signupRequest.getEmail());
        user.setPassword( passwordEncoder.encode(signupRequest.getPassword()) );
        user.setRole(Role.ROLE_USER);
        user.setImage("perfil.jpg");
        user.setBiografia(signupRequest.getBiografia());
        return user;
    }

}
