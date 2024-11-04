package org.example.construconectaapisql.service;

import org.example.construconectaapisql.model.ItemPedido;
import org.example.construconectaapisql.repository.ItemPedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemPedidoService {
    private final ItemPedidoRepository itemPedidoRepository;

    public ItemPedidoService(
            ItemPedidoRepository itemPedidoRepository
    ) {
        this.itemPedidoRepository = itemPedidoRepository;
    }

    public List<ItemPedido> findAllOrderItems() {
        return itemPedidoRepository.findAll();
    }

    @Transactional
    public ItemPedido saveOrderItems(ItemPedido orderItem) {
        return itemPedidoRepository.save(orderItem);
    }

    @Transactional
    public ItemPedido deleteOrderItems(Long orderItemId) {
        ItemPedido orderItem = findOrderItemsById(orderItemId);
        itemPedidoRepository.delete(orderItem);
        return orderItem;
    }

    public ItemPedido findOrderItemsById(Long orderItemId) {
        return itemPedidoRepository.findById(orderItemId)
                .orElseThrow(() -> new RuntimeException("Item Pedido não encontrado."));
    }

    public List<ItemPedido> findByProduct(Integer productId) {
        return itemPedidoRepository.findByProduto(productId);
    }

    public List<ItemPedido> findByOrder(Integer orderId) {
        return itemPedidoRepository.findByPedido(orderId);
    }
}
