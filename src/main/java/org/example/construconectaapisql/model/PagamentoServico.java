package org.example.construconectaapisql.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
@Table(name = "Pagamento_Servico")
public class PagamentoServico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pagamento_servico_id")
    @Schema(description = "Identificador Único do pagamento do serviço", example = "1")
    private Long pagamentoServicoId;

    @Column(name = "servico_id", nullable = false)
    @Schema(description = "Identificador Único do serviço relacionado ao pagamento", example = "101")
    private Integer servico;

    @Column(name = "usuario_id", nullable = false)
    @Schema(description = "Identificador Único do usuário que realizou o pagamento", example = "TwbSHSFVasyefyw42SFJAIoQDjJA")
    private String usuario;

    @Column(precision = 10, scale = 2, name = "valor_servico", nullable = false)
    @Schema(description = "Valor do serviço pago", example = "150.00")
    private BigDecimal valorServico;

    @Column(name = "tipo_pagamento", nullable = false)
    @Size(max = 20, message = "O tipo de pagamento deve ter no máximo 20 caracteres")
    @Schema(description = "Tipo de pagamento utilizado (e.g. Cartão, PIX)", example = "PIX")
    private String tipoPagamento;

    @Column(name = "data_pagamento", nullable = false)
    @Schema(description = "Data em que o pagamento foi realizado", example = "2024-10-13")
    private String dataPagamento;

    // Constructor
    public PagamentoServico() {}

    // Getters e Setters
    public Long getPagamentoServicoId() { return pagamentoServicoId; }
    public void setPagamentoServicoId(Long pagamentoServicoId) { this.pagamentoServicoId = pagamentoServicoId; }

    public Integer getServico() { return servico; }
    public void setServico(Integer servico) { this.servico = servico; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public BigDecimal getValorServico() { return valorServico; }
    public void setValorServico(BigDecimal valorServico) { this.valorServico = valorServico; }

    public String getTipoPagamento() { return tipoPagamento; }
    public void setTipoPagamento(String tipoPagamento) { this.tipoPagamento = tipoPagamento; }

    public String getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(String dataPagamento) { this.dataPagamento = dataPagamento; }

    @Override
    public String toString() {
        return "PagamentoServico{" +
                "pagamentoServicoId=" + pagamentoServicoId +
                ", servico=" + servico +
                ", usuario='" + usuario + '\'' +
                ", valorServico=" + valorServico +
                ", tipoPagamento='" + tipoPagamento + '\'' +
                ", dataPagamento=" + dataPagamento +
                '}';
    }
}
