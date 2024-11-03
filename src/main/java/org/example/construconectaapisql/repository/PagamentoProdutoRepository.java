package org.example.construconectaapisql.repository;

import org.example.construconectaapisql.model.PagamentoProduto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PagamentoProdutoRepository extends JpaRepository<PagamentoProduto, Long> {
    Optional<PagamentoProduto> findById(Long pagamentoPlanoId);

    List<PagamentoProduto> findByPedido(Integer pedido);

    List<PagamentoProduto> findByUsuario(String usuario);

    List<PagamentoProduto> findByDataPagamento(Date dataPagamento);

    List<PagamentoProduto> findByTipoPagamento(String tipoPagamento);
}
