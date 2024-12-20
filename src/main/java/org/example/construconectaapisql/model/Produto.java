package org.example.construconectaapisql.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Set;

@Entity
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "produto_id")
    @Schema(description = "Identificador Único do produto", example = "1")
    private Long produtoId;

    @Column(name = "nome_produto", nullable = false)
    @Size(min = 6, max = 250, message = "O nome do produto deve ter no mínimo 6 e no máximo 250 caracteres")
    @Schema(description = "Nome do produto", example = "Martelo de Construção Reforçado")
    private String nomeProduto;

    @NotNull
    @Schema(description = "Quantidade disponível no estoque", example = "100")
    private Integer estoque;

    @NotNull
    @Size(min = 10, max = 500, message = "A descrição deve ter no mínimo 10 e no máximo 500 caracteres")
    @Schema(description = "Descrição detalhada do produto", example = "Martelo de construção reforçado com cabo ergonômico.")
    private String descricao;

    @Column(precision = 10, scale = 2, nullable = false)
    @Schema(description = "Preço do produto", example = "59.99")
    private BigDecimal preco;

    @NotNull
    @Schema(description = "Condição do produto (true para novo, false para usado)", example = "true")
    private Boolean condicao;

    @Column(precision = 10, scale = 2)
    @Max(value = 1, message = "A porcentagem de desconto deve ser entre 0.0 e 1.0")
    @Schema(description = "Porcentagem de desconto aplicado no produto", example = "0.3")
    private BigDecimal desconto;

    @Size(max = 500, message = "A imagem deve ter no máximo 500 caracteres")
    @Schema(description = "URL da imagem do produto", example = "https://exemplo.com/imagem_produto.jpg")
    private String imagem;

    @Column(name = "usuario_id", nullable = false)
    @Schema(description = "UID do usuário que cadastrou o produto", example = "TwbSHSFVasyefyw42SFJAIoQDjJA")
    private String usuario;

    @NotNull(message = "O tópico é obrigatório")
    @Min(value = 1, message = "O tópico deve ser 1 (No Topo), 2 (Ofertas), 3 (Relevantes), 4 (Recomendados).")
    @Max(value = 4, message = "O tópico deve ser 1 (No Topo), 2 (Ofertas), 3 (Relevantes), 4 (Recomendados).")
    @Schema(description = "Tópico do produto - 1 (No Topo), 2 (Ofertas), 3 (Relevantes), 4 (Recomendados)", example = "3")
    private Integer topico;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Categoria_Produto",
            joinColumns = @JoinColumn(name = "produto_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Categoria> categorias;

    // Constructor
    public Produto() {}

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

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getImagem() { return imagem; }
    public void setImagem(String imagem) { this.imagem = imagem; }

    public Integer getTopico() { return topico; }
    public void setTopico(Integer topico) { this.topico = topico; }

    public Set<Categoria> getCategorias() { return categorias; }
    public void setCategorias(Set<Categoria> categorias) { this.categorias = categorias; }

    @Override
    public String toString() {
        return "Produto{" +
                "produtoId=" + produtoId +
                ", nomeProduto='" + nomeProduto + '\'' +
                ", estoque=" + estoque +
                ", descricao='" + descricao + '\'' +
                ", preco=" + preco +
                ", condicao=" + condicao +
                ", desconto=" + desconto +
                ", imagem='" + imagem + '\'' +
                ", usuario='" + usuario + '\'' +
                ", topico=" + topico +
                ", categorias=" + categorias +
                '}';
    }
}
