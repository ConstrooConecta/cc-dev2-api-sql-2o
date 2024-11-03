package org.example.construconectaapisql.service;

import org.example.construconectaapisql.model.Categoria;
import org.example.construconectaapisql.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) { this.categoriaRepository = categoriaRepository; }

    public List<Categoria> findAllCategories() { return categoriaRepository.findAll(); }

    @Transactional
    public Categoria saveCategories(Categoria categoria) {
        boolean  isUpdate = categoria.getCategoriaId() != null && categoriaRepository.existsById(categoria.getCategoriaId());
        validateUniqueFields(categoria, isUpdate);
        return categoriaRepository.save(categoria);
    }

    public Categoria findCategoriesById(Long categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
    }

    @Transactional
    public Categoria deleteCategory(Long categoriaId) {
        Categoria categoria = findCategoriesById(categoriaId);
        categoriaRepository.delete(categoria);
        return categoria;
    }

    public List<Categoria> findByNome(String nome) { return categoriaRepository.findByNomeLikeIgnoreCase(nome); }

    public boolean existsByNameIgnoreCase(String nome) {
        return categoriaRepository.existsByNomeIgnoreCase(nome);
    }

    private void validateUniqueFields(Categoria categoria, boolean isUpdate) {
        // Se não for uma atualização ou o CPF for diferente do CPF existente, validar
        if (!isUpdate || !categoriaRepository.findById(categoria.getCategoriaId()).get().getNome().equals(categoria.getNome())) {
            if (!categoriaRepository.findByNomeLikeIgnoreCase(categoria.getNome()).isEmpty()) {
                throw new RuntimeException("Categoria com este nome já existe.");
            }
        }
    }
}
