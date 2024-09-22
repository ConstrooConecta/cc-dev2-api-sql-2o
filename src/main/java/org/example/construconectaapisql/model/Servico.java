package org.example.construconectaapisql.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "servico_id")
    private Long servicoId;

    @NotNull
    @Size(max = 100, message = "O nome do serviço deve ter no máximo 100 caracteres")
    private String nomeServico;

    @NotNull
    @Size(max = 300, message = "A descrição deve ter no máximo 300 caracteres")
    private String descricao;

    @NotNull
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal preco;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Constructors, Getters and Setters
    public Servico() {}

    public Servico(Long servicoId, String nomeServico, String descricao, BigDecimal preco, Usuario usuario) {
        this.servicoId = servicoId;
        this.nomeServico = nomeServico;
        this.descricao = descricao;
        this.preco = preco;
        this.usuario = usuario;
    }

    // Getters and Setters
    public Long getServicoId() { return servicoId; }
    public void setServicoId(Long servicoId) { this.servicoId = servicoId; }

    public String getNomeServico() { return nomeServico; }
    public void setNomeServico(String nomeServico) { this.nomeServico = nomeServico; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
