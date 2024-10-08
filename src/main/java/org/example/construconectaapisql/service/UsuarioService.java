package org.example.construconectaapisql.service;

import org.example.construconectaapisql.model.Usuario;
import org.example.construconectaapisql.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> findAllUsers() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public Usuario saveUsers(Usuario usuario) {
        boolean isUpdate = usuario.getUid() != null && usuarioRepository.existsById(usuario.getUid());
        validateUniqueFields(usuario, isUpdate); // Validação de campos únicos, passando o estado de update
        return usuarioRepository.save(usuario);
    }

    public Usuario findUsersByUid(String uid) {
        return usuarioRepository.findById(uid)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
    }

    @Transactional
    public Usuario deleteUser(String uid) {
        Usuario usuario = findUsersByUid(uid);
        usuarioRepository.delete(usuario);
        return usuario;
    }

    public List<Usuario> findByNomeCompleto(String nomeCompleto) {
        return usuarioRepository.findByNomeCompletoLikeIgnoreCase(nomeCompleto);
    }

    public List<Usuario> findByNomeUsuario(String nomeUsuario) {
        return usuarioRepository.findByNomeUsuarioLikeIgnoreCase(nomeUsuario);
    }

    public List<Usuario> findByCpf(String cpf) {
        return usuarioRepository.findByCpf(cpf);
    }

    public List<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmailLikeIgnoreCase(email);
    }

    public List<Usuario> findByTelefone(String telefone) {
        return usuarioRepository.findByTelefone(telefone);
    }

    public List<Usuario> findByGenero(Integer genero) {
        return usuarioRepository.findByGenero(genero);
    }

    // Método para validar se os campos únicos já existem
    private void validateUniqueFields(Usuario usuario, boolean isUpdate) {
        // Se não for uma atualização ou o CPF for diferente do CPF existente, validar
        if (!isUpdate || !usuarioRepository.findById(usuario.getUid()).get().getCpf().equals(usuario.getCpf())) {
            if (!usuarioRepository.findByCpf(usuario.getCpf()).isEmpty()) {
                throw new RuntimeException("CPF já está em uso.");
            }
        }

        // Validação para e-mail
        if (!isUpdate || !usuarioRepository.findById(usuario.getUid()).get().getEmail().equalsIgnoreCase(usuario.getEmail())) {
            if (!usuarioRepository.findByEmailLikeIgnoreCase(usuario.getEmail()).isEmpty()) {
                throw new RuntimeException("E-mail já está em uso.");
            }
        }

        // Validação para nome de usuário
        if (!isUpdate || !usuarioRepository.findById(usuario.getUid()).get().getNomeUsuario().equalsIgnoreCase(usuario.getNomeUsuario())) {
            if (!usuarioRepository.findByNomeUsuarioLikeIgnoreCase(usuario.getNomeUsuario()).isEmpty()) {
                throw new RuntimeException("Nome de usuário já está em uso.");
            }
        }

        // Validação para telefone
        if (!isUpdate || !usuarioRepository.findById(usuario.getUid()).get().getTelefone().equals(usuario.getTelefone())) {
            if (!usuarioRepository.findByTelefone(usuario.getTelefone()).isEmpty()) {
                throw new RuntimeException("Telefone já está em uso.");
            }
        }
    }
}
