package org.example.construconectaapisql.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Date;

@Entity
public class PagamentoProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pagamento_produto_id")
    private Long pagamentoProdutoId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

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
    public PagamentoProduto() {}

    public PagamentoProduto(Long pagamentoProdutoId, Produto produto, Usuario usuario, BigDecimal valor, String tipoPagamento, Date dataPagamento) {
        this.pagamentoProdutoId = pagamentoProdutoId;
        this.produto = produto;
        this.usuario = usuario;
        this.valor = valor;
        this.tipoPagamento = tipoPagamento;
        this.dataPagamento = dataPagamento;
    }

    // Getters and Setters
    public Long getPagamentoProdutoId() { return pagamentoProdutoId; }
    public void setPagamentoProdutoId(Long pagamentoProdutoId) { this.pagamentoProdutoId = pagamentoProdutoId; }

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getTipoPagamento() { return tipoPagamento; }
    public void setTipoPagamento(String tipoPagamento) { this.tipoPagamento = tipoPagamento; }

    public Date getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(Date dataPagamento) { this.dataPagamento = dataPagamento; }
}
