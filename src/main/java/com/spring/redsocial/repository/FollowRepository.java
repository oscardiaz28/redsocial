package com.spring.redsocial.repository;

import com.spring.redsocial.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
    @Query(value = "SELECT * from follow where user_id = :user_id and followed_id = :followed_id", nativeQuery = true)
    Optional<Follow> isAlreadyFollow(@Param("user_id") Integer user_id, @Param("followed_id") Integer followed_id );

    @Query(value = "SELECT * from follow where user_id = :user_id", nativeQuery = true)
    /**
     * :user_id == marcador de posicion.
     * // @Param("user_id) == vincula el parametro con el marcador de posicion
     * Integer user_id == el valor que se reemplazara.
     */
    List<Follow> getFollowing(@Param("user_id") Integer user_id);

    @Query(value = "select * from follow where followed_id = :followed_id", nativeQuery = true)
    List<Follow> getFollowers(@Param("followed_id") Integer followed_id );

    @Query(value = "CALL getUserFollowing(:login_id, :current_id)" , nativeQuery = true)
    List<Follow> getUserFollowing(@Param("login_id") Integer login_id, @Param("current_id") Integer current_id );

    @Query(value = "CALL getUserFollowers(:login_id, :current_id)" , nativeQuery = true)
    List<Follow> getUserFollowers(@Param("login_id") Integer login_id, @Param("current_id") Integer current_id );

    @Query( value = "SELECT * FROM follow WHERE user_id = :login_id AND \n" +
            "followed_id IN (SELECT followed_id FROM users)" , nativeQuery = true)
    List<Follow> getUserFollowingFromList(@Param("login_id") Integer login_id );



}
