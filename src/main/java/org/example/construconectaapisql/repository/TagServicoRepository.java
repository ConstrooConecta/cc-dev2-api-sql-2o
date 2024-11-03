package org.example.construconectaapisql.repository;

import org.example.construconectaapisql.model.TagServico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagServicoRepository extends JpaRepository<TagServico, Long> {
    Optional<TagServico> findById(Long tagServicoId);

    List<TagServico> findByNomeLikeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCase(String nome);
}
