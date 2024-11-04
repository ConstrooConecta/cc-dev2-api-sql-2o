package org.example.construconectaapisql.service;

import org.example.construconectaapisql.model.Carrinho;
import org.example.construconectaapisql.model.Pedido;
import org.example.construconectaapisql.repository.CarrinhoRepository;
import org.example.construconectaapisql.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final CarrinhoRepository carrinhoRepository;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository, CarrinhoRepository carrinhoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.carrinhoRepository = carrinhoRepository;
    }

    public List<Pedido> findAllOrders() {
        return pedidoRepository.findAll();
    }

    @Transactional
    public Pedido saveOrders(Pedido pedido) {
        // Buscar o carrinho do usuário relacionado ao pedido
        List<Carrinho> carrinhos = carrinhoRepository.findByUsuario(pedido.getUsuario());

        // Se não houver carrinhos para o usuário, lança uma exceção
        if (carrinhos.isEmpty()) {
            throw new RuntimeException("Nenhum carrinho encontrado para o usuário: " + pedido.getUsuario());
        }

        // Calcular o valor total a partir do carrinho
        BigDecimal valorTotal = carrinhos.stream()
                .map(Carrinho::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // O frete será sempre 0, conforme solicitado
        BigDecimal valorFrete = BigDecimal.ZERO;

        // Definir o valorTotal e o valorFrete no pedido
        pedido.setValorTotal(valorTotal);
        pedido.setValorFrete(valorFrete);

        // Salvar o pedido
        return pedidoRepository.save(pedido);
    }

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

    public List<Pedido> findByCupom(String cupom) {
        return pedidoRepository.findByCupom(cupom);
    }

    public List<Pedido> findByDataPedido(String dataPedido) {
        return pedidoRepository.findByDataPedido(dataPedido);
    }

    public List<Pedido> findByDataEntrega(String dataEntrega) {
        return pedidoRepository.findByDataEntrega(dataEntrega);
    }
}
