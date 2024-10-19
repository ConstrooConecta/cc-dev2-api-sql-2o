package org.example.construconectaapisql.repository;

import org.example.construconectaapisql.model.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Integer> {
    Optional<Carrinho> findById(Integer carrinhoId);
    List<Carrinho> findByUsuario(String usuario);
    List<Carrinho> findByProduto(Integer produto);
    void deleteByUsuario(String usuario);
}
