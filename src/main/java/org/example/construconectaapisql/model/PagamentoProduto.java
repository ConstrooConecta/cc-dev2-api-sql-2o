package org.example.construconectaapisql.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
@Table(name = "Pagamento_Produto")
public class PagamentoProduto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pagamento_produto_id")
    @Schema(description = "Identificador Único do pagamento de produto", example = "1")
    private Long pagamentoProdutoId;

    @Column(name = "pedido_id", nullable = false)
    @Schema(description = "Identificador Único do pedido relacionado ao pagamento", example = "101")
    private Integer pedido;

    @Column(name = "usuario_id", nullable = false)
    @Size(min = 28, max = 28, message = "O UID deve ter 28 caracteres")
    @Schema(description = "UID do usuário que realizou o pagamento", example = "TwbSHSFVasyefyw42SFJAIoQDjJA")
    private String usuario;

    @Column(name = "data_pagamento", nullable = false)
    @Schema(description = "Data em que o pagamento foi realizado")
    private String dataPagamento;

    @Column(name = "tipo_pagamento", nullable = false)
    @Size(max = 20, message = "O tipo de pagamento deve ter no máximo 20 caracteres")
    @Schema(description = "Tipo de pagamento utilizado (e.g. Cartão, PIX)", example = "PIX")
    private String tipoPagamento;

    @Column(precision = 10, scale = 2, name = "valor_total", nullable = false)
    @Schema(description = "Valor total do pagamento", example = "199.99")
    private BigDecimal valorTotal;

    @Column(precision = 10, scale = 2, name = "valor_frete", nullable = false)
    @Schema(description = "Valor do frete associado ao pagamento", example = "19.99")
    private BigDecimal valorFrete;

    // Constructor
    public PagamentoProduto() {}

    // Getters e Setters
    public Long getPagamentoProdutoId() { return pagamentoProdutoId; }
    public void setPagamentoProdutoId(Long pagamentoProdutoId) { this.pagamentoProdutoId = pagamentoProdutoId; }

    public Integer getPedido() { return pedido; }
    public void setPedido(Integer pedido) { this.pedido = pedido; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(String dataPagamento) { this.dataPagamento = dataPagamento; }

    public String getTipoPagamento() { return tipoPagamento; }
    public void setTipoPagamento(String tipoPagamento) { this.tipoPagamento = tipoPagamento; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public BigDecimal getValorFrete() { return valorFrete; }
    public void setValorFrete(BigDecimal valorFrete) { this.valorFrete = valorFrete; }

    @Override
    public String toString() {
        return "PagamentoProduto{" +
                "pagamentoProdutoId=" + pagamentoProdutoId +
                ", pedido=" + pedido +
                ", usuario='" + usuario + '\'' +
                ", dataPagamento=" + dataPagamento +
                ", tipoPagamento='" + tipoPagamento + '\'' +
                ", valorTotal=" + valorTotal +
                ", valorFrete=" + valorFrete +
                '}';
    }
}
