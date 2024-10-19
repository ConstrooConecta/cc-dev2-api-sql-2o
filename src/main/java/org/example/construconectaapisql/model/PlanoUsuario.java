package org.example.construconectaapisql.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
@Table(name = "Plano_Usuario")
public class PlanoUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plano_usuario_id")
    @Schema(description = "Identificador único do Plano do Usuário", example = "1")
    private Long planoUsuarioId;

    @Column(name = "usuario_id", nullable = false)
    @Size(min = 28, max = 28, message = "O nome do usuário deve ter 28 caracteres.")
    @Schema(description = "UID do Usuário que possui um Plano", example = "TwbSHSFVasyefyw42SFJAIoQDjJA")
    private String usuario;

    @Column(name = "plano_id", nullable = false)
    @Schema(description = "Identificador único do Plano", example = "2")
    private Integer plano;

    @Column(name = "data_assinatura", nullable = false)
    @Schema(description = "Data de assinatura do Plano adquirido")
    @Temporal(TemporalType.DATE)
    private Date dataAssinatura;

    @Column(name = "data_final", nullable = false)
    @Schema(description = "Data de expiração do Plano adquirido")
    @Temporal(TemporalType.DATE)
    private Date dataFinal;

    // Constructors, Getters and Setters
    public PlanoUsuario() {}

    public PlanoUsuario(
            Long planoUsuarioId,
            String usuario,
            Integer plano,
            Date dataAssinatura,
            Date dataFinal
    ) {
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

    @Override
    public String toString() {
        return "PlanoUsuario{" +
                "planoUsuarioId=" + planoUsuarioId +
                ", usuario='" + usuario + '\'' +
                ", plano=" + plano +
                ", dataAssinatura=" + dataAssinatura +
                ", dataFinal=" + dataFinal +
                '}';
    }
}
