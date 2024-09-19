package org.example.construconectaapisql.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plano_id")
    private Long planoId;

    @NotNull
    @Size(min = 1, max = 100, message = "O nome do plano deve ter no máximo 100 caracteres")
    private String nome;

    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
    private String descricao;

    @NotNull
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;

    // Constructors, Getters and Setters
    public Plano() {}

    public Plano(Long planoId, String nome, String descricao, BigDecimal valor) {
        this.planoId = planoId;
        this.nome = nome;
        this.descricao = descricao;
        this.valor = valor;
    }

    // Getters and Setters
    public Long getPlanoId() { return planoId; }
    public void setPlanoId(Long planoId) { this.planoId = planoId; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
}
