package com.spring.redsocial.service;

import com.spring.redsocial.exception.RedSocialExceptionHandler;
import com.spring.redsocial.model.User;
import com.spring.redsocial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow( () -> new RedSocialExceptionHandler(
            "El email no existe"
        ) );
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                getAuthorities("ROLE_USER")
        );
    }
    private Collection<? extends GrantedAuthority> getAuthorities(String rol){
        return Collections.singletonList(new SimpleGrantedAuthority(rol));
    }

}
