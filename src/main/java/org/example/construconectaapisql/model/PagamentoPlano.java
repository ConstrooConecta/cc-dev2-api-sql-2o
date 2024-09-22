package org.example.construconectaapisql.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Date;

@Entity
public class PagamentoPlano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pagamento_plano_id")
    private Long pagamentoPlanoId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "plano_id", nullable = false)
    private Plano plano;

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
    public PagamentoPlano() {}

    public PagamentoPlano(Long pagamentoPlanoId, Plano plano, Usuario usuario, BigDecimal valor, String tipoPagamento, Date dataPagamento) {
        this.pagamentoPlanoId = pagamentoPlanoId;
        this.plano = plano;
        this.usuario = usuario;
        this.valor = valor;
        this.tipoPagamento = tipoPagamento;
        this.dataPagamento = dataPagamento;
    }

    // Getters and Setters
    public Long getPagamentoPlanoId() { return pagamentoPlanoId; }
    public void setPagamentoPlanoId(Long pagamentoPlanoId) { this.pagamentoPlanoId = pagamentoPlanoId; }

    public Plano getPlano() { return plano; }
    public void setPlano(Plano plano) { this.plano = plano; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getTipoPagamento() { return tipoPagamento; }
    public void setTipoPagamento(String tipoPagamento) { this.tipoPagamento = tipoPagamento; }

    public Date getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(Date dataPagamento) { this.dataPagamento = dataPagamento; }
}
