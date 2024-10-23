package org.example.construconectaapisql.repository;

import org.example.construconectaapisql.model.Servico;
import org.example.construconectaapisql.model.TagServico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
    Optional<Servico> findById(Long servicoId);
    List<Servico> findByUsuario(String usuario);
    List<Servico> findByTagServicos(Set<TagServico> tagServicos);
}
