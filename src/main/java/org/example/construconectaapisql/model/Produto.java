package org.example.construconectaapisql.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "produto_id")
    @JsonIgnore
    private Long produtoId;

    @NotNull
    @Size(min = 6, max = 250, message = "O nome do produto deve ter no mínimo 6 e no máximo 250 caracteres")
    private String nomeProduto;

    @NotNull
    private Integer estoque;

    @NotNull
    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    private String descricao;

    @NotNull
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal preco;

    @NotNull
    private Boolean condicao;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal desconto;

/*    @Column(precision = 10, scale = 2, nullable = false)
    @JsonIgnore
    private BigDecimal total;

    // Método que é chamado antes de persistir a entidade no banco de dados
    @PrePersist
    @PreUpdate
    public void calcularTotal() {
        if (this.total == null && this.preco != null && this.desconto != null) {
            this.total = this.preco.subtract(this.desconto);
        }
    }*/

    @Column(name = "usuario_id", nullable = false)
    private String usuarioId;

    // Constructors, Getters and Setters
    public Produto() {}

    public Produto(
            Long produtoId,
            String nomeProduto,
            Integer estoque,
            String descricao,
            BigDecimal preco,
            Boolean condicao,
            BigDecimal desconto,
//            BigDecimal total,
            String usuarioId
    ) {
        this.produtoId = produtoId;
        this.nomeProduto = nomeProduto;
        this.estoque = estoque;
        this.descricao = descricao;
        this.preco = preco;
        this.condicao = condicao;
        this.desconto = desconto;
//        this.total = total;
        this.usuarioId = usuarioId;
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

/*    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }*/

    public String getUsuario() { return usuarioId; }
    public void setUsuario(String usuarioId) { this.usuarioId = usuarioId; }
}
