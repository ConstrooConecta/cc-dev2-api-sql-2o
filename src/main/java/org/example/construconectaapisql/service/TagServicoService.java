package org.example.construconectaapisql.service;

import org.example.construconectaapisql.model.TagServico;
import org.example.construconectaapisql.repository.TagServicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagServicoService {
    private final TagServicoRepository tagServicoRepository;

    public TagServicoService(TagServicoRepository tagServicoRepository) {
        this.tagServicoRepository = tagServicoRepository;
    }

    public List<TagServico> findAllTags() {
        return tagServicoRepository.findAll();
    }

    @Transactional
    public TagServico saveTags(TagServico tagServico) {
        boolean isUpdate = tagServico.getTagServicoId() != null && tagServicoRepository.existsById(tagServico.getTagServicoId());
        validateUniqueFields(tagServico, isUpdate);
        return tagServicoRepository.save(tagServico);
    }

    public TagServico findTagsById(Long tagServicoId) {
        return tagServicoRepository.findById(tagServicoId)
                .orElseThrow(() -> new RuntimeException("Tag de Serviço não encontrada."));
    }

    @Transactional
    public TagServico deleteTag(Long tagServicoId) {
        TagServico tagServico = findTagsById(tagServicoId);
        tagServicoRepository.delete(tagServico);
        return tagServico;
    }

    public List<TagServico> findByNome(String nome) {
        return tagServicoRepository.findByNomeLikeIgnoreCase(nome);
    }

    public boolean existsByNameIgnoreCase(String nome) {
        return tagServicoRepository.existsByNomeIgnoreCase(nome);
    }

    private void validateUniqueFields(TagServico tagServico, boolean isUpdate) {
        // Se não for uma atualização ou o CPF for diferente do CPF existente, validar
        if (!isUpdate || !tagServicoRepository.findById(tagServico.getTagServicoId()).get().getNome().equals(tagServico.getNome())) {
            if (!tagServicoRepository.findByNomeLikeIgnoreCase(tagServico.getNome()).isEmpty()) {
                throw new RuntimeException("Tag de Serviço com este nome já existe.");
            }
        }
    }
}
