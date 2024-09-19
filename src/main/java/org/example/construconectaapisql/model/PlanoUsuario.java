package org.example.construconectaapisql.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class PlanoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plano_usuario_id")
    private Long planoUsuarioId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "plano_id", nullable = false)
    private Plano plano;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date dataAssinatura;

    @Temporal(TemporalType.DATE)
    private Date dataFinal;

    @NotNull
    private Boolean ativacao;

    // Constructors, Getters and Setters
    public PlanoUsuario() {}

    public PlanoUsuario(Long planoUsuarioId, Usuario usuario, Plano plano, Date dataAssinatura, Date dataFinal, Boolean ativacao) {
        this.planoUsuarioId = planoUsuarioId;
        this.usuario = usuario;
        this.plano = plano;
        this.dataAssinatura = dataAssinatura;
        this.dataFinal = dataFinal;
        this.ativacao = ativacao;
    }

    // Getters and Setters
    public Long getPlanoUsuarioId() { return planoUsuarioId; }
    public void setPlanoUsuarioId(Long planoUsuarioId) { this.planoUsuarioId = planoUsuarioId; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Plano getPlano() { return plano; }
    public void setPlano(Plano plano) { this.plano = plano; }

    public Date getDataAssinatura() { return dataAssinatura; }
    public void setDataAssinatura(Date dataAssinatura) { this.dataAssinatura = dataAssinatura; }

    public Date getDataFinal() { return dataFinal; }
    public void setDataFinal(Date dataFinal) { this.dataFinal = dataFinal; }

    public Boolean getAtivacao() { return ativacao; }
    public void setAtivacao(Boolean ativacao) { this.ativacao = ativacao; }
}
