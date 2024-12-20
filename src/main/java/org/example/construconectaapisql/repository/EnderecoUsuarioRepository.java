package org.example.construconectaapisql.repository;

import org.example.construconectaapisql.model.EnderecoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnderecoUsuarioRepository extends JpaRepository<EnderecoUsuario, Long> {
    Optional<EnderecoUsuario> findById(Long enderecoUsuarioId);

    List<EnderecoUsuario> findByCep(String cep);

    List<EnderecoUsuario> findByUf(String uf);

    List<EnderecoUsuario> findByCidade(String cidade);

    List<EnderecoUsuario> findByBairro(String bairro);

    List<EnderecoUsuario> findByRua(String rua);

    List<EnderecoUsuario> findByUsuario(String usuario);
}
