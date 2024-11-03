package org.example.construconectaapisql.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoria_id")
    @Schema(description = "Identificador Único da categoria", example = "1")
    private Long categoriaId;

    @NotBlank(message = "O nome da categoria é obrigatório.")
    @Size(max = 250, message = "O nome da categoria deve ter no máximo 250 caracteres")
    @Schema(description = "Nome da categoria", example = "Azulejos")
    @Column(unique = true, nullable = false)
    private String nome;

    public Categoria() {
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "categoriaId=" + categoriaId +
                ", nome='" + nome + '\'' +
                '}';
    }
}
