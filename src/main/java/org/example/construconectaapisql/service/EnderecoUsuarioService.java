package org.example.construconectaapisql.service;

import org.example.construconectaapisql.model.EnderecoUsuario;
import org.example.construconectaapisql.repository.EnderecoUsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnderecoUsuarioService {
    private final EnderecoUsuarioRepository enderecoUsuarioRepository;

    public EnderecoUsuarioService(EnderecoUsuarioRepository enderecoUsuarioRepository) {
        this.enderecoUsuarioRepository = enderecoUsuarioRepository;
    }

    public List<EnderecoUsuario> findAllAddress() { return enderecoUsuarioRepository.findAll(); }

    @Transactional
    public EnderecoUsuario saveAddress(EnderecoUsuario address) { return enderecoUsuarioRepository.save(address); }

}
