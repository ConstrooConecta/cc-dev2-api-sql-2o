package org.example.construconectaapisql.service;

import org.example.construconectaapisql.model.Categoria;
import org.example.construconectaapisql.model.Produto;
import org.example.construconectaapisql.repository.CategoriaRepository;
import org.example.construconectaapisql.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository) { this.produtoRepository = produtoRepository; }

    public List<Produto> findAllProducts() { return produtoRepository.findAll(); }

    @Transactional
    public Produto saveProducts(Produto produto) { return produtoRepository.save(produto); }

    public Produto findProductsById(Long produtoId) {
        return produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado."));
    }

    @Transactional
    public Produto deleteProduct(Long produtoId) {
        Produto produto = findProductsById(produtoId);
        produtoRepository.delete(produto);
        return produto;
    }

    public List<Produto> findByCategorias(List<Categoria> categorias) {
        return produtoRepository.findByCategorias(new HashSet<>(categorias));
    }

    // Atualize o método que busca categorias se não estiver definido
    public List<Categoria> findCategoriasByNome(String nome) {
        String nomeNormalizado = normalizeString(nome);
        List<Categoria> categorias = categoriaRepository.findAll();
        return categorias.stream()
                .filter(c -> normalizeString(c.getNome()).contains(nomeNormalizado))
                .collect(Collectors.toList());
    }

    private String normalizeString(String input) {
        // Remove os acentos
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
    }

    public List<Produto> findByNomeProduto(String nomeProduto) { return produtoRepository.findByNomeProdutoLikeIgnoreCase(nomeProduto); }

    public List<Produto> findByCondicao(Boolean condicao) { return produtoRepository.findByCondicao(condicao); }

    public List<Produto> findByUserId(String usuario) { return produtoRepository.findByUsuario(usuario); }

    public List<Produto> findByTopico(Integer topico) { return produtoRepository.findByTopico(topico); }

    public Optional<Produto> findByProdutoId(Long produtoId) { return produtoRepository.findById(produtoId); }
}
