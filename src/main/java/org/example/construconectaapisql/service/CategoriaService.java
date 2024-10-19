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
    public Categoria saveCategories(Categoria categoria) { return categoriaRepository.save(categoria); }

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

}
