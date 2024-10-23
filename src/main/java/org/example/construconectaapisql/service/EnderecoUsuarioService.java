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

    // CRUD
    public List<EnderecoUsuario> findAllAddress() { return enderecoUsuarioRepository.findAll(); }

    @Transactional
    public EnderecoUsuario saveAddress(EnderecoUsuario address) { return enderecoUsuarioRepository.save(address); }

    @Transactional
    public EnderecoUsuario deleteAddress(Long enderecoUsuarioId) {
        EnderecoUsuario address = findAddressById(enderecoUsuarioId);
        enderecoUsuarioRepository.delete(address);
        return address;
    }

    public EnderecoUsuario findAddressById(Long enderecoUsuarioId) {
        return enderecoUsuarioRepository.findById(enderecoUsuarioId)
                .orElseThrow(() -> new RuntimeException("Endereço não encontrado."));
    }

    // Others
    public List<EnderecoUsuario> findByCep(String cep) { return enderecoUsuarioRepository.findByCep(cep); }
    public List<EnderecoUsuario> findByUf(String uf) { return enderecoUsuarioRepository.findByUf(uf); }
    public List<EnderecoUsuario> findByCidade(String cidade) { return enderecoUsuarioRepository.findByCidade(cidade); }
    public List<EnderecoUsuario> findByBairro(String bairro) { return enderecoUsuarioRepository.findByBairro(bairro); }
    public List<EnderecoUsuario> findByRua(String rua) { return enderecoUsuarioRepository.findByRua(rua); }
    public List<EnderecoUsuario> findByUserId(String usuario) { return enderecoUsuarioRepository.findByUsuario(usuario); }
}
