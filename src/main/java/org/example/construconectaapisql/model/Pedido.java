package org.example.construconectaapisql.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;

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

    @Column(precision = 10, scale = 2, name = "valor_total", nullable = false)
    @Schema(description = "Valor total do pedido", example = "159.99")
    private BigDecimal valorTotal;

    @Column(precision = 10, scale = 2, name = "valor_frete", nullable = false)
    @Schema(description = "Valor total do frete", example = "7.99")
    private BigDecimal valorFrete;

    @Column(name = "cupom")
    @Schema(description = "Cupom de desconto aplicado ao pedido", example = "CONSTROO20")
    @Max(value = 20, message = "O cupom deve ter no máximo 20 caracteres")
    private String cupom;

    @Column(precision = 10, scale = 2, name = "valor_desconto")
    @Max(value = 1, message = "A porcentagem de desconto deve ser entre 0.0 e 1.0")
    @Schema(description = "Valor do desconto (em porcentagem) oferecido pelo cupom", example = "0.20")
    private BigDecimal valorDesconto;

    @Column(name = "data_pedido", nullable = false)
    @Schema(description = "Data em que o pedido foi realizado")
    @Temporal(TemporalType.DATE)
    private Date dataPedido;

    @Column(name = "data_entrega", nullable = false)
    @Schema(description = "Data estimada para entrega do pedido")
    @Temporal(TemporalType.DATE)
    private Date dataEntrega;

    public Pedido() {
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public void setValorFrete(BigDecimal valorFrete) {
        this.valorFrete = valorFrete;
    }

    public void setDataPedido(Date dataPedido) {
        this.dataPedido = dataPedido;
    }

    public void setDataEntrega(Date dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "pedidoId=" + pedidoId +
                ", usuario='" + usuario + '\'' +
                ", valorTotal=" + valorTotal +
                ", valorFrete=" + valorFrete +
                ", cupom='" + cupom + '\'' +
                ", valorDesconto=" + valorDesconto +
                ", dataPedido=" + dataPedido +
                ", dataEntrega=" + dataEntrega +
                '}';
    }
}