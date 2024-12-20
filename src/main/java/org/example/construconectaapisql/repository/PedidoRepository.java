package org.example.construconectaapisql.repository;

import org.example.construconectaapisql.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Optional<Pedido> findById(Long pedidoId);

    List<Pedido> findByUsuario(String usuario);

    List<Pedido> findByCupom(String cupom);

    List<Pedido> findByDataPedido(String dataPedido);

    List<Pedido> findByDataEntrega(String dataEntrega);
}
