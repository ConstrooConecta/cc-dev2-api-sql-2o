package org.example.construconectaapisql.service;

import org.example.construconectaapisql.model.Servico;
import org.example.construconectaapisql.model.TagServico;
import org.example.construconectaapisql.repository.ServicoRepository;
import org.example.construconectaapisql.repository.TagServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicoService {
    private final ServicoRepository servicoRepository;

    @Autowired
    private TagServicoRepository tagServicoRepository;

    public ServicoService(ServicoRepository servicoRepository) { this.servicoRepository = servicoRepository; }

    // CRUD
    @Transactional
    public Servico saveServices(Servico servico) { return servicoRepository.save(servico); }
    @Transactional
    public Servico deleteService(Long servicoId) {
        Servico servico = findServicoById(servicoId);
        servicoRepository.delete(servico);
        return servico;
    }
    public List<Servico> findAllServices() { return servicoRepository.findAll(Sort.by(Sort.Direction.ASC, "servicoId")); }

    // Other
    public Servico findServicoById(Long servicoId) {
        return servicoRepository.findById(servicoId)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado."));
    }
    public List<Servico> findByServiceName(String nomeServico) {
        String nomeNormalizado = normalizeString(nomeServico);
        List<Servico> servicos = servicoRepository.findAll();
        return servicos.stream()
                .filter(c -> normalizeString(c.getNomeServico()).contains(nomeNormalizado))
                .collect(Collectors.toList());
    }
    public List<Servico> findByUserId(String usuario) { return servicoRepository.findByUsuario(usuario); }
    public List<Servico> findByServicesTag(List<TagServico> tagServico) { return servicoRepository.findByTagServicos(new HashSet<>(tagServico)); }
    public List<TagServico> findByTagServiceName(String nome) {
        String nomeNormalizado = normalizeString(nome);
        List<TagServico> servicos = tagServicoRepository.findAll();
        return servicos.stream()
                .filter(c -> normalizeString(c.getNome()).contains(nomeNormalizado))
                .collect(Collectors.toList());
    }

    private String normalizeString(String input) {
        // Remove os acentos
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
    }
}
