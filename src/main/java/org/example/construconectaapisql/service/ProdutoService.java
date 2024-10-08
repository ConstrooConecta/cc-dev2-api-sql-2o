package org.example.construconectaapisql.service;

import org.example.construconectaapisql.model.Produto;
import org.example.construconectaapisql.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> findAllProducts() {
        return produtoRepository.findAll();
    }

    @Transactional
    public Produto saveProducts(Produto produto) {
        return produtoRepository.save(produto);
    }

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

    public List<Produto> findByNomeProduto(String nomeProduto) {
        return produtoRepository.findByNomeProdutoLikeIgnoreCase(nomeProduto);
    }

    public List<Produto> findByCondicao(Boolean condicao) {
        return produtoRepository.findByCondicao(condicao);
    }

    public List<Produto> findByUserId (String usuarioId) {
        return produtoRepository.findByUsuarioId(usuarioId);
    }

    public Optional<Produto> findByProdutoId(Long produtoId) {
        return produtoRepository.findById(produtoId);
    }
}
