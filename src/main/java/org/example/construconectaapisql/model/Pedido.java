package org.example.construconectaapisql.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pedido_id")
    private Long pedidoId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date dataPedido;

    @NotNull
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal valorTotal;

    @NotNull
    private Boolean pagamentoConcluido;

    @ManyToOne
    @JoinColumn(name = "carrinho_id", nullable = false)
    private Carrinho carrinho;

    // Constructors, Getters and Setters
    public Pedido() {}

    public Pedido(Long pedidoId, Usuario usuario, Date dataPedido, BigDecimal valorTotal, Boolean pagamentoConcluido, Carrinho carrinho) {
        this.pedidoId = pedidoId;
        this.usuario = usuario;
        this.dataPedido = dataPedido;
        this.valorTotal = valorTotal;
        this.pagamentoConcluido = pagamentoConcluido;
        this.carrinho = carrinho;
    }

    // Getters and Setters
    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Date getDataPedido() { return dataPedido; }
    public void setDataPedido(Date dataPedido) { this.dataPedido = dataPedido; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public Boolean getPagamentoConcluido() { return pagamentoConcluido; }
    public void setPagamentoConcluido(Boolean pagamentoConcluido) { this.pagamentoConcluido = pagamentoConcluido; }

    public Carrinho getCarrinho() { return carrinho; }
    public void setCarrinho(Carrinho carrinho) { this.carrinho = carrinho; }
}
