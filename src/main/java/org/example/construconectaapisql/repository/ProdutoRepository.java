package org.example.construconectaapisql.repository;

import org.example.construconectaapisql.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Optional<Produto> findById(Long produtoId);
    List<Produto> findByNomeProdutoLikeIgnoreCase(String nomeProduto);
    List<Produto> findByCondicao(Boolean condicao);
    List<Produto> findByUsuarioId(String usuarioId);
}
