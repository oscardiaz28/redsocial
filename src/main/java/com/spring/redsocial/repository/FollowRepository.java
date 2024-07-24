package com.spring.redsocial.repository;

import com.spring.redsocial.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
    @Query(value = "SELECT * from follow where user_id = :user_id and followed_id = :followed_id", nativeQuery = true)
    Optional<Follow> isAlreadyFollow(@Param("user_id") Integer user_id, @Param("followed_id") Integer followed_id );
}
