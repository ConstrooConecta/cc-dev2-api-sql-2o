package org.example.construconectaapisql.service;

import org.example.construconectaapisql.model.Pedido;
import org.example.construconectaapisql.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PedidoService {
    private PedidoRepository pedidoRepository;

    @Autowired
    public PedidoService(
            PedidoRepository pedidoRepository
    ) { this.pedidoRepository = pedidoRepository; }

    public List<Pedido> findAllOrders() { return pedidoRepository.findAll(); }

    @Transactional
    public Pedido saveOrders(Pedido pedido) { return pedidoRepository.save(pedido); }

    @Transactional
    public Pedido deleteOrders(Long pedidoId) {
        Pedido pedido = findOrdersBydId(pedidoId);
        pedidoRepository.delete(pedido);
        return pedido;
    }

    public Pedido findOrdersBydId(Long pedidoId) {
        return pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado."));
    }

    public List<Pedido> findByUsuario(String usuario) { return pedidoRepository.findByUsuario(usuario); }
    public List<Pedido> findByCupom(String cupom) { return pedidoRepository.findByCupom(cupom); }
    public List<Pedido> findByDataPedido(Date dataPedido) { return pedidoRepository.findByDataPedido(dataPedido); }
    public List<Pedido> findByDataEntrega(Date dataEntrega) { return pedidoRepository.findByDataEntrega(dataEntrega); }
}
