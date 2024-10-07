package org.example.construconectaapisql.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;
import java.util.Date;

@Entity
public class Usuario {

    @Id
    @Column(name = "uid", nullable = false, unique = true)
    @Size(min = 28, max = 28, message = "O UID deve ter 28 caracteress")
    private String uid;

    @Column(name = "nome_completo", nullable = false)
    @Size(min = 6, max = 300, message = "O nome deve ter no mínimo 6 e no máximo 300 caracteres")
    private String nomeCompleto;

    @Column(name = "nome_usuario", nullable = false, unique = true)
    @Size(min = 5, max = 20, message = "O nome de usuário deve ter no mínimo 5  e no máximo 20 caracteres")
    private String nomeUsuario;

    @CPF(message = "CPF Inválido!")
    @Column(nullable = false, unique = true)
    @Size(min = 11, max = 11, message = "O CPF deve ter 11 caracteres")
    private String cpf;

    @Column(nullable = false, unique = true)
    @Size(min = 5, max = 250, message = "O e-mail deve ter no mínimo 5 e no máximo 250 caracteres.")
    private String email;

    @NotNull
    @Size(min = 8, max = 500, message = "A senha deve ter no mínimo 8 e no máximo 500 caracteres")
    private String senha;

    @NotNull
    @Size(min = 11, max = 11, message = "O telefone deve ter 11 caracteres")
    private String telefone;

    @Column(name = "data_nascimento", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;

    @NotNull
    private Integer genero;

    // Constructors, Getters and Setters
    public Usuario() {}

    public Usuario(
            String uid,
            String nomeCompleto,
            String nomeUsuario,
            String cpf,
            String email,
            String senha,
            String telefone,
            Date dataNascimento,
            Integer genero
    ) {
        this.uid = uid;
        this.nomeCompleto = nomeCompleto;
        this.nomeUsuario = nomeUsuario;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.genero = genero;
    }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getNomeCompleto() {
        return nomeCompleto;
    }
    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }
    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }
    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Integer getGenero() { return genero; }
    public void setGenero(Integer genero) { this.genero = genero; }

    @Override
    public String toString() {
        return "Usuario{" +
                "uid='" + uid + '\'' +
                ", nomeCompleto='" + nomeCompleto + '\'' +
                ", nomeUsuario='" + nomeUsuario + '\'' +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", telefone='" + telefone + '\'' +
                ", dataNascimento=" + dataNascimento +
                ", genero=" + genero +
                '}';
    }

}
