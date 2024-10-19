package org.example.construconectaapisql.repository;

import org.example.construconectaapisql.model.Plano;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanoRepository extends JpaRepository<Plano, Long> {
    Optional<Plano> findById(Long planoId);
}

