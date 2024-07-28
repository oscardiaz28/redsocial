package com.spring.redsocial.repository;

import com.spring.redsocial.model.Publication;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PublicationRepository extends JpaRepository<Publication, Integer> {
    List<Publication> findAllByUserId(Integer id, Sort sort);
    Optional<Publication> findByIdAndUserId(Integer id, Integer userId);
    @Query(value = "CALL getFeed(:id)", nativeQuery = true)
    List<Publication> getFeed( @Param("id") Integer id);

    long countByUserId(Integer id);
}
