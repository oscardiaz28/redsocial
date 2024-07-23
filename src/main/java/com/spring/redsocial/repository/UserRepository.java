package com.spring.redsocial.repository;

import com.spring.redsocial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByNick(String nick);
    Optional<User> findByEmail(String email);
    List<User> findByEmailOrNick(String email, String nick);

}
