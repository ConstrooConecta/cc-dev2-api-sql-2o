package org.example.construconectaapisql.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Pedido {
    @Id
    @Column(name = "pedido_id")
    @Schema(description = "Identificador Único do pedido", example = "1")
    private Long pedidoId;

    @Column(name = "usuario_id", nullable = false)
    @Schema(description = "UID do usuário que fez o pedido", example = "TwbSHSFVasyefyw42SFJAIoQDjJA")
    private String usuario;

    @Column(name = "carrinho_id", nullable = false)
    @Schema(description = "Identificador Único do carrinho relacionado ao pedido", example = "6")
    private Integer carrinho;

    @Column(name = "data_pedido", nullable = false)
    @Schema(description = "Data em que o pedido foi realizado")
    @Temporal(TemporalType.DATE)
    private Date dataPedido;

    @Column(name = "data_entrega", nullable = false)
    @Schema(description = "Data estimada para entrega do pedido")
    @Temporal(TemporalType.DATE)
    private Date dataEntrega;

    @Column(precision = 10, scale = 2, name = "valor_total", nullable = false)
    @Schema(description = "Valor total do pedido", example = "159.99")
    private BigDecimal valorTotal;

    @Column(precision = 10, scale = 2, name = "valor_frete")
    @Schema(description = "Valor total do frete", example = "22.99")
    private BigDecimal valorFrete;

    public Pedido(){}

    public Pedido(
            Long pedidoId,
            String usuario,
            Integer carrinho,
            Date dataPedido,
            Date dataEntrega,
            BigDecimal valorTotal,
            BigDecimal valorFrete) {
        this.pedidoId = pedidoId;
        this.usuario = usuario;
        this.carrinho = carrinho;
        this.dataPedido = dataPedido;
        this.dataEntrega = dataEntrega;
        this.valorTotal = valorTotal;
        this.valorFrete = valorFrete;
    }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public Integer getCarrinho() { return carrinho; }
    public void setCarrinho(Integer carrinho) { this.carrinho = carrinho; }

    public Date getDataPedido() { return dataPedido; }
    public void setDataPedido(Date dataPedido) { this.dataPedido = dataPedido; }

    public Date getDataEntrega() { return dataEntrega; }
    public void setDataEntrega(Date dataEntrega) { this.dataEntrega = dataEntrega; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public BigDecimal getValorFrete() { return valorFrete; }
    public void setValorFrete(BigDecimal valorFrete) { this.valorFrete = valorFrete; }

    @Override
    public String toString() {
        return "Pedido{" +
                "pedidoId=" + pedidoId +
                ", usuario='" + usuario + '\'' +
                ", carrinho=" + carrinho +
                ", dataPedido=" + dataPedido +
                ", dataEntrega=" + dataEntrega +
                ", valorTotal=" + valorTotal +
                ", valorFrete=" + valorFrete +
                '}';
    }
}