package org.example.construconectaapisql.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class EnderecoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "endereco_usuario_id")
    private Long enderecoUsuarioId;

    @NotNull
    @Size(min = 8, max = 8, message = "O CEP deve ter exatamente 8 caracteres")
    private String cep;

    @NotNull
    @Size(min = 2, max = 2, message = "A UF deve ter exatamente 2 caracteres")
    private String uf;

    @NotNull
    @Size(min = 1, max = 23, message = "A cidade deve ter no máximo 23 caracteres")
    private String cidade;

    @Size(max = 53, message = "O bairro deve ter no máximo 53 caracteres")
    private String bairro;

    @NotNull
    @Size(min = 1, max = 50, message = "A rua deve ter no máximo 50 caracteres")
    private String rua;

    @Size(max = 20, message = "O número deve ter no máximo 20 caracteres")
    private String numero;

    @Size(max = 250, message = "O complemento deve ter no máximo 250 caracteres")
    private String complemento;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Constructors, Getters and Setters
    public EnderecoUsuario() {}

    public EnderecoUsuario(Long enderecoUsuarioId, String cep, String uf, String cidade, String bairro, String rua, String numero, String complemento, Usuario usuario) {
        this.enderecoUsuarioId = enderecoUsuarioId;
        this.cep = cep;
        this.uf = uf;
        this.cidade = cidade;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
        this.usuario = usuario;
    }

    // Getters and Setters
    public Long getEnderecoUsuarioId() { return enderecoUsuarioId; }
    public void setEnderecoUsuarioId(Long enderecoUsuarioId) { this.enderecoUsuarioId = enderecoUsuarioId; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
