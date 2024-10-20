package org.example.construconectaapisql.repository;

import org.example.construconectaapisql.model.Categoria;
import org.example.construconectaapisql.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Optional<Produto> findById(Long produtoId);
    List<Produto> findByNomeProdutoLikeIgnoreCase(String nomeProduto);
    List<Produto> findByCondicao(Boolean condicao);
    List<Produto> findByUsuario(String usuario);
    List<Produto> findByTopico(Integer topico);
    List<Produto> findByCategorias(Set<Categoria> categorias);

    List<Produto> findByNomeCategoria(String nome);
}
