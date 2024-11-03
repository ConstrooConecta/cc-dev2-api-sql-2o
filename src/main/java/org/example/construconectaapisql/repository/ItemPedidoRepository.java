package org.example.construconectaapisql.repository;

import org.example.construconectaapisql.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    Optional<ItemPedido> findById(Long itemPedidoId);
    List<ItemPedido> findByProduto(Integer produto);
    List<ItemPedido> findByPedido(Integer pedido);
}
