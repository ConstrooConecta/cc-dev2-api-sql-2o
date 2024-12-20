package org.example.construconectaapisql.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Entity
public class TagServico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_servico_id")
    @Schema(description = "Identificador Único da Tag do Serviço", example = "1")
    private Long tagServicoId;

    @NotBlank(message = "O nome da Tag é obrigatório")
    @Size(max = 100, message = "O nome da Tag deve ter no máximo 100 caracteres")
    @Schema(description = "Nome da Tag de Serviço", example = "Encanador")
    private String nome;

    @NotNull(message = "O preço médio é obrigatório")
    @Column(name = "preco_medio", precision = 10, scale = 2)
    @Schema(description = "Preço Médio do Serviço", example = "359.99")
    private BigDecimal precoMedio;

    // Constructor
    public TagServico() {}

    // Getters and Setters
    public Long getTagServicoId() { return tagServicoId; }
    public void setTagServicoId(Long tagServicoId) { this.tagServicoId = tagServicoId; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public BigDecimal getPrecoMedio() { return precoMedio; }
    public void setPrecoMedio(BigDecimal precoMedio) { this.precoMedio = precoMedio; }

    @Override
    public String toString() {
        return "TagServico{" +
                "tagServicoId=" + tagServicoId +
                ", nome='" + nome + '\'' +
                ", precoMedio=" + precoMedio +
                '}';
    }
}
