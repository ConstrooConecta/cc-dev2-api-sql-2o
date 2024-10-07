package org.example.construconectaapisql.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Carrinho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carrinho_id")
    private Long carrinhoId;

    @Column(name = "usuario_id", nullable = false)
    private String usuario;

    @Column(name = "produto_id", nullable = false)
    private Integer produto;

    private Integer quantidade;

    @Column(name = "produtos_img")
    private String produtoImg;

    @Column(name = "valor_total", nullable = false)
    private BigDecimal valorTotal;

    // Constructors
    public Carrinho() {}

    // Getters and Setters
    public Long getCarrinhoId() { return carrinhoId; }
    public void setCarrinhoId(Long carrinhoId) { this.carrinhoId = carrinhoId; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public Integer getProduto() { return produto; }
    public void setProduto(Integer produto) { this.produto = produto; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public String getProdutoImg() { return produtoImg; }
    public void setProdutoImg(String produtoImg) { this.produtoImg = produtoImg; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
}
