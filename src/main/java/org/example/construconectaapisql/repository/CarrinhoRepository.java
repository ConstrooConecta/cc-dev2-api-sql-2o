package org.example.construconectaapisql.repository;

import org.example.construconectaapisql.model.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
    Optional<Carrinho> findById(Long carrinhoId);

    List<Carrinho> findByUsuario(String usuario);

    List<Carrinho> findByProduto(Integer produto);

    List<Carrinho> findByIdentificador(Integer identificador);

    @Query("SELECT COALESCE(MAX(c.identificador), 0) FROM Carrinho c")
    Integer findMaxIdentificador();

    void deleteByUsuario(String usuario);

    void deleteByIdentificador(Integer identificador);
}
