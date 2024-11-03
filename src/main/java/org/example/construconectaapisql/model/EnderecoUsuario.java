package org.example.construconectaapisql.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Endereco_Usuariox")
public class EnderecoUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "endereco_usuario_id")
    @Schema(description = "Identificador único do Endereço do Usuário", example = "1")
    private Long enderecoUsuarioId;

    @NotBlank(message = "O CEP é obrigatório")
    @Size(min = 8, max = 8, message = "O CEP deve ter 8 caracteres")
    @Schema(description = "Código de Endereçamento Postal do Endereço do Usuário", example = "01310930")
    private String cep;

    @NotBlank(message = "O estado é obrigatório")
    @Size(min = 2, max = 2, message = "A UF deve ter 2 caracteres")
    @Schema(description = "Estado onde resida o Usuário", example = "SP")
    private String uf;

    @NotBlank(message = "A cidade é obrigatória")
    @Size(max = 23, message = "A cidade deve ter no máximo 23 caracteres")
    @Schema(description = "Cidade onde resida o Usuário", example = "São Paulo")
    private String cidade;

    @NotBlank(message = "O bairro é obrigatório")
    @Size(min = 1, max = 53, message = "O bairro deve ter no mínimo 1 e no máximo 53 caracteres")
    @Schema(description = "Bairro onde resida o Usuário", example = "Osasco")
    private String bairro;

    @NotBlank(message = "A rua é obrigatória")
    @Size(max = 75, message = "A rua deve ter no máximo 50 caracteres")
    @Schema(description = "Rua onde resida o Usuário", example = "Av. Brigadeiro Faria Lima")
    private String rua;

    @Size(max = 20, message = "O número deve ter no máximo 20 caracteres")
    @Schema(description = "Número da casa onde resida o Usuário", example = "132H")
    private String numero;

    @Size(max = 150, message = "O complemento deve ter no máximo 150 caracteres")
    @Schema(description = "Complemento do Endereço do Usuário", example = "Frente à praça")
    private String complemento;

    @NotBlank(message = "O UID do usuário é obrigatório")
    @Column(name = "usuario_id")
    @Size(min = 28, max = 28, message = "O UID do usuário deve ter 28 caracteres")
    @Schema(description = "UID do Usuário que possui o endereço", example = "TwbSHSFVasyefyw42SFJAIoQDjJA")
    private String usuario;

    // Constructors, Getters and Setters
    public EnderecoUsuario() {
    }

    // Getters and Setters
    public void setCep(String cep) {
        this.cep = cep;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "EnderecoUsuario{" +
                "enderecoUsuarioId=" + enderecoUsuarioId +
                ", cep='" + cep + '\'' +
                ", uf='" + uf + '\'' +
                ", cidade='" + cidade + '\'' +
                ", bairro='" + bairro + '\'' +
                ", rua='" + rua + '\'' +
                ", numero='" + numero + '\'' +
                ", complemento='" + complemento + '\'' +
                ", usuario='" + usuario + '\'' +
                '}';
    }
}
