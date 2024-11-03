package org.example.construconectaapisql.repository;

import org.example.construconectaapisql.model.PlanoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PlanoUsuarioRepository extends JpaRepository<PlanoUsuario, Long> {
    Optional<PlanoUsuario> findById(Long planoUsuarioId);

    List<PlanoUsuario> findByPlano(Integer plano);

    List<PlanoUsuario> findByUsuario(String usuario);

    List<PlanoUsuario> findByDataAssinatura(Date dataAssinatura);

    List<PlanoUsuario> findByDataFinal(Date dataFinal);
}
