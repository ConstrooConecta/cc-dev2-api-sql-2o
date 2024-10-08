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

    public List<EnderecoUsuario> findAllAddress() {
        return enderecoUsuarioRepository.findAll();
    }

    @Transactional
    public EnderecoUsuario saveAddress(EnderecoUsuario endereco) {
        return enderecoUsuarioRepository.save(endereco);
    }

    public EnderecoUsuario findAddressById(Long enderecoUsuarioId) {
        return enderecoUsuarioRepository.findById(enderecoUsuarioId)
                .orElseThrow(() -> new RuntimeException("Endereco não encontrado."));
    }

    @Transactional
    public EnderecoUsuario deleteAddress(Long enderecoUsuarioId) {
        EnderecoUsuario enderecoUsuario = findAddressById(enderecoUsuarioId);
        enderecoUsuarioRepository.delete(enderecoUsuario);
        return enderecoUsuario;
    }

    public List<EnderecoUsuario> findByCep(String cep) { return this.enderecoUsuarioRepository.findByCep(cep); }

    public List<EnderecoUsuario> findByCidade(String cidade) { return this.enderecoUsuarioRepository.findByCidade(cidade); }

    public List<EnderecoUsuario> findByUf(String uf) { return this.enderecoUsuarioRepository.findByUf(uf); }

    public List<EnderecoUsuario> findByBairro(String bairro) { return this.enderecoUsuarioRepository.findByBairro(bairro); }

    public List<EnderecoUsuario> findByRua(String rua) { return this.enderecoUsuarioRepository.findByRua(rua); }

    public List<EnderecoUsuario> findByUsuario(String usuario) { return this.enderecoUsuarioRepository.findByUsuario(usuario); }

}
