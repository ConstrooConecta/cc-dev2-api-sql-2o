package org.example.construconectaapisql.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class ItemPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_pedido_id")
    @Schema(description = "Identificador Único do Item do Pedido", example = "1")
    private Long itemPedidoId;

    @Column(name = "produto_id", nullable = false)
    @Schema(description = "Identificador Único do produto comprado no pedido", example = "101")
    private Integer produto;

    @Column(name = "pedido_id", nullable = false)
    @Schema(description = "Identificador Único do pedido", example = "87")
    private Integer pedido;

    @Column(name = "quantidade", nullable = false)
    @Schema(description = "Quantidade do produto no pedido", example = "2")
    private Integer quantidade;

    @Column(precision = 10, scale = 2, name = "preco_unitario", nullable = false)
    @Schema(description = "Preço unitário do produto no pedido", example = "324.97")
    private BigDecimal precoUnitario;

    public ItemPedido() {
    }

    public Integer getProduto() {
        return produto;
    }

    public void setProduto(Integer produto) {
        this.produto = produto;
    }

    public Integer getPedido() {
        return pedido;
    }

    public void setPedido(Integer pedido) {
        this.pedido = pedido;
    }

    @Override
    public String toString() {
        return "ItemPedido{" +
                "itemPedidoId=" + itemPedidoId +
                ", produto=" + produto +
                ", pedido=" + pedido +
                ", quantidade=" + quantidade +
                ", precoUnitario=" + precoUnitario +
                '}';
    }
}
