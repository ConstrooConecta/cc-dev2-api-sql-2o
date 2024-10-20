package org.example.construconectaapisql.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
public class Carrinho {
    @Id
    @Column(name = "carrinho_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador Único único do carrinho", example = "1")
    private Integer carrinhoId;

    @Column(name = "usuario_id", nullable = false)
    @Size(min = 28, max = 28, message = "O UID do usuário deve ter 28 caracteres")
    @Schema(description = "UID do usuário associado ao carrinho", example = "TwbSHSFVasyefyw42SFJAIoQDjJA")
    private String usuario;

    @Column(name = "produto_id", nullable = false)
    @Schema(description = "Identificador Único do produto adicionado ao carrinho", example = "101")
    private Integer produto;

    @Column(name = "produtos_img")
    @Size(max = 500, message = "A URI da imagem deve ter no máximo 500 caracteres")
    @Schema(description = "Imagem do produto no carrinho (URI)", example = "https://example.com/produto.jpg")
    private String produtoImg;

    @NotNull(message = "A quantidade de produtos no carrinho é obrigatória.")
    @Schema(description = "Quantidade de unidades do produto no carrinho", example = "2")
    private Integer quantidade;

    @Column(precision = 10, scale = 2, name = "valor_total", nullable = false)
    @Schema(description = "Valor total do carrinho", example = "499.99")
    private BigDecimal valorTotal;

    // Construtores
    public Carrinho() {
    }

    public Carrinho(
            Integer carrinhoId,
            String usuario,
            Integer produto,
            String produtoImg,
            Integer quantidade,
            BigDecimal valorTotal
    ) {
        this.carrinhoId = carrinhoId;
        this.usuario = usuario;
        this.produto = produto;
        this.produtoImg = produtoImg;
        this.quantidade = quantidade;
        this.valorTotal = valorTotal;
    }

    // Getters e Setters
    public Integer getCarrinhoId() {
        return carrinhoId;
    }

    public void setCarrinhoId(Integer carrinhoId) {
        this.carrinhoId = carrinhoId;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Integer getProduto() {
        return produto;
    }

    public void setProduto(Integer produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getProdutoImg() {
        return produtoImg;
    }

    public void setProdutoImg(String produtoImg) {
        this.produtoImg = produtoImg;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    @Override
    public String toString() {
        return "Carrinho{" +
                "carrinhoId=" + carrinhoId +
                ", usuario='" + usuario + '\'' +
                ", produto=" + produto +
                ", produtoImg='" + produtoImg + '\'' +
                ", quantidade=" + quantidade +
                ", valorTotal=" + valorTotal +
                '}';
    }
}
