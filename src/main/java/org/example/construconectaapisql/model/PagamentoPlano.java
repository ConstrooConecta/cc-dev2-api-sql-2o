package org.example.construconectaapisql.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
@Table(name = "Pagamento_Plano")
public class PagamentoPlano {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pagamento_plano_id")
    @Schema(description = "Identificador único do Pagamento do Plano.", example = "1")
    private Long pagamentoPlanoId;

    @Column(name = "plano_id", nullable = false)
    @Schema(description = "Identificador do Plano adquirido pelo Usuário.", example = "3")
    private Integer plano;

    @Column(name = "usuario_id", nullable = false)
    @Schema(description = "UID do Usuário que fez o pagamento.", example = "TwbSHSFVasyefyw42SFJAIoQDjJA")
    private String usuario;

    @Column(precision = 10, scale = 2, nullable = false)
    @Schema(description = "Valor do Pagamento.", example = "199.99")
    private BigDecimal valor;

    @Column(name = "tipo_pagamento", nullable = false)
    @Size(max = 20, message = "O tipo de pagamento deve ter no máximo 20 caracteres.")
    @Schema(description = "Método de Pagamento utilizado na compra do plano.", example = "PIX")
    private String tipoPagamento;

    @Column(name = "data_pagamento", nullable = false)
    @Schema(description = "Data do Pagamento.", example = "2024-10-01")
    private String dataPagamento;

    // Constructor
    public PagamentoPlano() {}

    // Getters and Setters
    public Long getPagamentoPlanoId() { return pagamentoPlanoId; }
    public void setPagamentoPlanoId(Long pagamentoPlanoId) { this.pagamentoPlanoId = pagamentoPlanoId; }

    public Integer getPlano() { return plano; }
    public void setPlano(Integer plano) { this.plano = plano; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getTipoPagamento() { return tipoPagamento; }
    public void setTipoPagamento(String tipoPagamento) { this.tipoPagamento = tipoPagamento; }

    public String getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(String dataPagamento) { this.dataPagamento = dataPagamento; }

    @Override
    public String toString() {
        return "PagamentoPlano{" +
                "pagamentoPlanoId=" + pagamentoPlanoId +
                ", plano=" + plano +
                ", usuario='" + usuario + '\'' +
                ", valor=" + valor +
                ", tipoPagamento='" + tipoPagamento + '\'' +
                ", dataPagamento=" + dataPagamento +
                '}';
    }
}
