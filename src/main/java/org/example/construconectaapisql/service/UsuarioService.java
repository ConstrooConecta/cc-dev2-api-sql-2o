package org.example.construconectaapisql.service;

import org.example.construconectaapisql.model.Usuario;
import org.example.construconectaapisql.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) { this.usuarioRepository = usuarioRepository; }

    public List<Usuario> findAllUsers() { return usuarioRepository.findAll(); }

    @Transactional
    public Usuario saveUsers( Usuario usuario ) { return usuarioRepository.save( usuario ); }

    public Usuario findUsersByUid ( String uid ) {
        return usuarioRepository.findById(uid).orElseThrow(() ->
                new RuntimeException("Cliente não encontrado."));
    }

    @Transactional
    public Usuario deleteUser ( String uid ) {
        Usuario usuario = findUsersByUid(uid);
        usuarioRepository.delete(usuario);
        return usuario;
    }

    public List<Usuario> findByNomeCompleto ( String nomeCompleto ) { return usuarioRepository.findByNomeCompletoLikeIgnoreCase(nomeCompleto); }
    public List<Usuario> findByNomeUsuario ( String nomeUsuario ) { return usuarioRepository.findByNomeUsuarioLikeIgnoreCase(nomeUsuario); }
    public List<Usuario> findByCpf ( String cpf ) { return usuarioRepository.findByCpf(cpf); }
    public List<Usuario> findByEmail ( String email ) { return usuarioRepository.findByEmailLikeIgnoreCase(email); }
    public List<Usuario> findByTelefone ( String telefone ) { return usuarioRepository.findByTelefone(telefone); }
    public List<Usuario> findByGenero ( Integer genero ) { return usuarioRepository.findByGenero(genero); }

}
