package org.example.construconectaapisql.repository;

import org.example.construconectaapisql.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findById(String uuid);
    List<Usuario> findByNomeCompletoLikeIgnoreCase(String nomeCompleto);
    List<Usuario> findByNomeUsuarioLikeIgnoreCase(String nomeUsuario);
    List<Usuario> findByCpf(String cpf);
    List<Usuario> findByEmailLikeIgnoreCase(String email);
    List<Usuario> findByTelefone(String telefone);
    List<Usuario> findByGenero(Integer genero);
}
