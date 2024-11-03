package org.example.construconectaapisql.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Date;

@Entity
public class Usuario {
    @Id
    @Column(name = "uid", nullable = false, unique = true)
    @Size(min = 28, max = 28, message = "O UID deve ter 28 caracteres.")
    @Schema(description = "UID do Usuário.", example = "TwbSHSFVasyefyw42SFJAIoQDjJA")
    private String uid;

    @Column(name = "nome_completo", nullable = false)
    @NotBlank(message = "O nome completo é obrigatório.")
    @Size(min = 5, max = 300, message = "O nome completo deve ter entre 5 e 100 caracteres.")
    @Schema(description = "Nome Completo do Usuário.", example = "Olecram Olodom ad Avlis")
    private String nomeCompleto;

    @Column(name = "nome_usuario", nullable = false, unique = true)
    @NotBlank(message = "O nome de usuário é obrigatório.")
    @Size(min = 5, max = 20, message = "O nome de usuário deve ter entre 5 e 20 caracteres.")
    @Pattern(regexp = "^[a-zA-Z0-9._]+$", message = "O nome de usuário deve conter apenas letras, números, pontos e sublinhados.")
    @Schema(description = "Nome de usuário (identifação) do Usuário (no aplicativo). O nome de usuário deve conter apenas letras, números, pontos e sublinhados.", example = "olecram.olodom24")
    private String nomeUsuario;

    @CPF(message = "CPF Inválido!")
    @NotBlank(message = "O CPF é obrigatório.")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve ter 11 dígitos.")
    @Schema(description = "Cadastro de Pessoa Física do Usuário (válido).", example = "12345678910")
    private String cpf;

    @Email(message = "O e-mail deve ser válido.")
    @Size(min = 5, max = 250, message = "O e-mail deve ter no mínimo 5 e no máximo 250 caracteres.")
    @NotBlank(message = "O e-mail é obrigatório.")
    @Schema(description = "E-mail do Usuário.", example = "olecramolodum@adsilva.com")
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 8, max = 500, message = "A senha deve ter pelo menos 8 caracteres e no máximo 500.")
    @Schema(description = "Senha do Usuário.", example = "admin123")
    private String senha;

    @NotBlank(message = "O telefone é obrigatório.")
    @Pattern(regexp = "^\\d{11}$", message = "O telefone deve ter 11 dígitos.")
    @Schema(description = "Telefone do Usuário.", example = "11991234568")
    private String telefone;

    @Column(name = "data_nascimento", nullable = false)
    @Schema(description = "Data de nascimento do Usuário.")
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;

    @NotNull(message = "O gênero é obrigatório.")
    @Min(value = 1, message = "O gênero deve ser 1 (masculino), 2 (feminino), 3 (outro) ou 4 (prefiro não dizer).")
    @Max(value = 4, message = "O gênero deve ser 1 (masculino), 2 (feminino), 3 (outro) ou 4 (prefiro não dizer).")
    @Schema(description = "Gênero do Usuário - 1 (masculino), 2 (feminino), 3 (outro) ou 4 (prefiro não dizer).", example = "3")
    private Integer genero;

    // Constructors, Getters and Setters
    public Usuario() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setGenero(Integer genero) {
        this.genero = genero;
    }

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
