package org.example.construconectaapisql.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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

    public TagServico() {}

    public TagServico(
            Long tagServicoId,
            String nome
    ) {
        this.tagServicoId = tagServicoId;
        this.nome = nome;
    }

    public Long getTagServicoId() { return tagServicoId; }
    public void setTagServicoId(Long tagServicoId) { this.tagServicoId = tagServicoId; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    @Override
    public String toString() {
        return "TagServico{" +
                "tagServicoId=" + tagServicoId +
                ", nome='" + nome + '\'' +
                '}';
    }
}
