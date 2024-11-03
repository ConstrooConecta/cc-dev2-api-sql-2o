package org.example.construconectaapisql.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
public class Plano {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plano_id")
    @Schema(description = "Identificador único do Plano", example = "1")
    private Long planoId;

    @Column(nullable = false, unique = true)
    @Size(max = 100, message = "O nome do plano deve ter no máximo 100 caracteres")
    @Schema(description = "Nome do Plano", example = "Premium")
    private String nome;

    @NotBlank(message = "A descrição do plano é obrigatória")
    @Size(min = 3, max = 255, message = "A descrição deve ter no mínimo 3 e no máximo 255 caracteres")
    @Schema(description = "Descrição (vantagens) do Plano",
            example = "- Sem Anúncios" +
                      "\n- Sem Serviço" +
                      "\n- Cupons exclusivos")
    private String descricao;

    @NotNull(message = "O valor do plano é obrigatório")
    @Column(precision = 10, scale = 2)
    @Schema(description = "Valor do Plano", example = "32.90")
    private BigDecimal valor;

    // Constructors, Getters and Setters
    public Plano() {}

    public Plano(
            Long planoId,
            String nome,
            String descricao,
            BigDecimal valor
    ) {
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

    @Override
    public String toString() {
        return "Plano{" +
                "planoId=" + planoId +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                '}';
    }
}
