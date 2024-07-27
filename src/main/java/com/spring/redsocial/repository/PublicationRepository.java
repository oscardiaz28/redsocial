package com.spring.redsocial.repository;

import com.spring.redsocial.model.Publication;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicationRepository extends JpaRepository<Publication, Integer> {
    List<Publication> findAllByUserId(Integer id, Sort sort);
}
