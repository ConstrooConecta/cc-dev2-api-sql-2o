package org.example.construconectaapisql.repository;

import org.example.construconectaapisql.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findById(Long categoriaId);
    List<Categoria> findByNomeLikeIgnoreCase(String nome);
    boolean existsByNomeIgnoreCase(String nome);
}
