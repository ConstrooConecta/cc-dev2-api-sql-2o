package org.example.construconectaapisql.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Date;

@Entity
public class PlanoUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plano_usuario_id")
    private Long planoUsuarioId;

    @Column(name = "usuario_id", nullable = false)
    private String usuario;

    @Column(name = "plano_id", nullable = false)
    private Integer plano;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_assinatura", nullable = false)
    private Date dataAssinatura;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_final", nullable = false)
    private Date dataFinal;

    // Constructors, Getters and Setters
    public PlanoUsuario() {}

    public PlanoUsuario(Long planoUsuarioId, String usuario, Integer plano, Date dataAssinatura, Date dataFinal) {
        this.planoUsuarioId = planoUsuarioId;
        this.usuario = usuario;
        this.plano = plano;
        this.dataAssinatura = dataAssinatura;
        this.dataFinal = dataFinal;
    }

    // Getters and Setters
    public Long getPlanoUsuarioId() { return planoUsuarioId; }
    public void setPlanoUsuarioId(Long planoUsuarioId) { this.planoUsuarioId = planoUsuarioId; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public Integer getPlano() { return plano; }
    public void setPlano(Integer plano) { this.plano = plano; }

    public Date getDataAssinatura() { return dataAssinatura; }
    public void setDataAssinatura(Date dataAssinatura) { this.dataAssinatura = dataAssinatura; }

    public Date getDataFinal() { return dataFinal; }
    public void setDataFinal(Date dataFinal) { this.dataFinal = dataFinal; }
}
