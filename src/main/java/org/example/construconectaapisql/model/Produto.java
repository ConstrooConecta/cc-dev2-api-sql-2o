package org.example.construconectaapisql.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "produto_id")
    private Long produtoId;

    @NotNull
    @Size(max = 100, message = "O nome do produto deve ter no máximo 100 caracteres")
    private String nomeProduto;

    @NotNull
    private Integer estoque;

    @NotNull
    @Size(max = 300, message = "A descrição deve ter no máximo 300 caracteres")
    private String descricao;

    @NotNull
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal preco;

    @NotNull
    private Boolean condicao;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal desconto;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Constructors, Getters and Setters
    public Produto() {}

    public Produto(Long produtoId, String nomeProduto, Integer estoque, String descricao, BigDecimal preco, Boolean condicao, BigDecimal desconto, Usuario usuario) {
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.estoque = estoque;
        this.descricao = descricao;
        this.preco = preco;
        this.condicao = condicao;
        this.desconto = desconto;
        this.usuario = usuario;
    }

    // Getters and Setters
    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }

    public String getNomeProduto() { return nomeProduto; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }

    public Integer getEstoque() { return estoque; }
    public void setEstoque(Integer estoque) { this.estoque = estoque; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public Boolean getCondicao() { return condicao; }
    public void setCondicao(Boolean condicao) { this.condicao = condicao; }

    public BigDecimal getDesconto() { return desconto; }
    public void setDesconto(BigDecimal desconto) { this.desconto = desconto; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
