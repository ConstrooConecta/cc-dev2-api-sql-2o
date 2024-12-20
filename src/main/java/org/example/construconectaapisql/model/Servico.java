package org.example.construconectaapisql.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Set;

@Entity
public class Servico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "servico_id")
    @Schema(description = "Identificador Único do serviço", example = "1")
    private Long servicoId;

    @Column(name = "nome_servico", nullable = false)
    @Size(max = 100, message = "O nome do serviço deve ter no máximo 100 caracteres")
    @Schema(description = "Nome do serviço", example = "Instalação de Elétrica Residencial")
    private String nomeServico;

    @NotNull
    @Size(max = 500, message = "A descrição deve ter no máximo 300 caracteres")
    @Schema(description = "Descrição detalhada do serviço", example = "Serviço completo de instalação elétrica residencial com certificação de segurança.")
    private String descricao;

    @NotNull
    @Column(precision = 10, scale = 2, nullable = false)
    @Schema(description = "Preço do serviço", example = "350.00")
    private BigDecimal preco;

    @Column(name = "usuario_id", nullable = false)
    @Schema(description = "UID do Usuário que oferece o serviço", example = "TwbSHSFVasyefyw42SFJAIoQDjJA")
    private String usuario;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "TagServico_Servico",
            joinColumns = @JoinColumn(name = "servico_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_servico_id")
    )
    private Set<TagServico> tagServicos;

    // Constructor
    public Servico() {}

    // Getters e Setters
    public Long getServicoId() { return servicoId; }
    public void setServicoId(Long servicoId) { this.servicoId = servicoId; }

    public String getNomeServico() { return nomeServico; }
    public void setNomeServico(String nomeServico) { this.nomeServico = nomeServico; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public Set<TagServico> getTagServicos() { return tagServicos; }
    public void setTagServicos(Set<TagServico> tagServicos) { this.tagServicos = tagServicos; }

    @Override
    public String toString() {
        return "Servico{" +
                "servicoId=" + servicoId +
                ", nomeServico='" + nomeServico + '\'' +
                ", descricao='" + descricao + '\'' +
                ", preco=" + preco +
                ", usuario=" + usuario +
                ", tagServicos=" + tagServicos +
                '}';
    }
}
