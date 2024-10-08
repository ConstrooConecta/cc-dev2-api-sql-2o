package org.example.construconectaapisql.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;
import java.util.Date;

@Entity
public class Usuario {

    @Id
    @Column(name = "uid", nullable = false, unique = true)
    @Size(min = 28, max = 28, message = "O UID deve ter 28 caracteress")
    private String uid;

    @Column(name = "nome_completo", nullable = false)
    @NotBlank(message = "O nome completo é obrigatório.")
    @Size(min = 5, max = 100, message = "O nome completo deve ter entre 5 e 100 caracteres.")
    private String nomeCompleto;

    @Column(name = "nome_usuario", nullable = false, unique = true)
    @NotBlank(message = "O nome de usuário é obrigatório.")
    @Size(min = 5, max = 20, message = "O nome de usuário deve ter entre 5 e 20 caracteres.")
    @Pattern(regexp = "^[a-zA-Z0-9._]+$", message = "O nome de usuário deve conter apenas letras, números, pontos e sublinhados.")
    private String nomeUsuario;

    @CPF(message = "CPF Inválido!")
    @NotBlank(message = "O CPF é obrigatório.")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve ter 11 dígitos.")
    private String cpf;

    @Email(message = "O e-mail deve ser válido.")
    @Size(min = 5, max = 250, message = "O e-mail deve ter no mínimo 5 e no máximo 250 caracteres.")
    @NotBlank(message = "O e-mail é obrigatório.")
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres.")
    private String senha;

    @NotBlank(message = "O telefone é obrigatório.")
    @Pattern(regexp = "^\\d{11}$", message = "O telefone deve ter 11 dígitos.")
    private String telefone;

    @Column(name = "data_nascimento", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;

    @NotNull(message = "O gênero é obrigatório.")
    @Min(value = 1, message = "O gênero deve ser 1 (masculino), 2 (feminino), 3 (outro) ou 4 (prefiro não dizer).")
    @Max(value = 4, message = "O gênero deve ser 1 (masculino), 2 (feminino), 3 (outro) ou 4 (prefiro não dizer).")
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
