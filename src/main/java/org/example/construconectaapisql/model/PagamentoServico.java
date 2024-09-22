package org.example.construconectaapisql.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Date;

@Entity
public class PagamentoServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pagamento_servico_id")
    private Long pagamentoServicoId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotNull
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;

    @NotNull
    @Size(max = 20, message = "O tipo de pagamento deve ter no máximo 20 caracteres")
    private String tipoPagamento;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date dataPagamento;

    // Constructors, Getters and Setters
    public PagamentoServico() {}

    public PagamentoServico(Long pagamentoServicoId, Servico servico, Usuario usuario, BigDecimal valor, String tipoPagamento, Date dataPagamento) {
        this.pagamentoServicoId = pagamentoServicoId;
        this.servico = servico;
        this.usuario = usuario;
        this.valor = valor;
        this.tipoPagamento = tipoPagamento;
        this.dataPagamento = dataPagamento;
    }

    // Getters and Setters
    public Long getPagamentoServicoId() { return pagamentoServicoId; }
    public void setPagamentoServicoId(Long pagamentoServicoId) { this.pagamentoServicoId = pagamentoServicoId; }

    public Servico getServico() { return servico; }
    public void setServico(Servico servico) { this.servico = servico; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getTipoPagamento() { return tipoPagamento; }
    public void setTipoPagamento(String tipoPagamento) { this.tipoPagamento = tipoPagamento; }

    public Date getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(Date dataPagamento) { this.dataPagamento = dataPagamento; }
}
