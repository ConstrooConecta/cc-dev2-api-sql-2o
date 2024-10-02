package org.example.construconectaapisql.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;
import java.util.Date;
import java.util.Set;

@Entity
public class Usuario {

    @Id
    @Column(name = "uid", nullable = false, unique = true)
    @Size(min = 28, max = 28, message = "O UID deve ter 28 caracteress")
    private String uid;

    @CPF(message = "CPF Inválido!")
    @Column(nullable = false, unique = true)
    @Size(min = 11, max = 11, message = "O CPF deve ter exatamente 11 caracteres")
    private String cpf;

    @Column(name = "nome_completo", nullable = false)
    @Size(min = 6, max = 300, message = "O nome deve ter no mínimo 6 e no máximo 300 caracteres")
    private String nomeCompleto;

    @Column(name = "nome_usuario", nullable = false, unique = true)
    @Size(min = 6, max = 20, message = "O nome de usuário deve ter no mínimo 6 e no máximo 20 caracteres")
    private String nomeUsuario;

    @Column(nullable = false, unique = true)
    @Size(min = 5, max = 250, message = "O e-mail deve ter no mínimo 5 e no máximo 250 caracteres.")
    private String email;

    @NotNull
    @Size(min = 8, max = 500, message = "A senha deve ter no mínimo 8 e no máximo 500 caracteres")
    private String senha;

    @NotNull
    @Size(min = 11, max = 11, message = "O telefone deve ter 11 caracteres")
    private String telefone;

    @NotNull
    private Integer genero;

    @Column(name = "data_nascimento", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    private Set<EnderecoUsuario> enderecos;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    private Set<PlanoUsuario> planosUsuarios;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    private Set<PagamentoPlano> pagamentosPlanos;

/*    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    private Set<Produto> produtos;*/

    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    private Set<Servico> servicos;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    private Set<Carrinho> carrinhos;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    private Set<Pedido> pedidos;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    private Set<PagamentoProduto> pagamentosProdutos;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario")
    private Set<PagamentoServico> pagamentosServicos;

    // Constructors, Getters and Setters
    public Usuario() {}

    public Usuario(
            String uid,
            String cpf,
            String nomeCompleto,
            String nomeUsuario,
            String email,
            String senha,
            String telefone,
            Integer genero,
            Date dataNascimento
    ) {
        this.uid = uid;
        this.cpf = cpf;
        this.nomeCompleto = nomeCompleto;
        this.nomeUsuario = nomeUsuario;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.genero = genero;
        this.dataNascimento = dataNascimento;
    }

    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

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

    public Integer getGenero() {
        return genero;
    }

    public void setGenero(Integer genero) {
        this.genero = genero;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Set<EnderecoUsuario> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(Set<EnderecoUsuario> enderecos) {
        this.enderecos = enderecos;
    }

    public Set<PlanoUsuario> getPlanosUsuarios() {
        return planosUsuarios;
    }

    public void setPlanosUsuarios(Set<PlanoUsuario> planosUsuarios) {
        this.planosUsuarios = planosUsuarios;
    }

    public Set<PagamentoPlano> getPagamentosPlanos() {
        return pagamentosPlanos;
    }

    public void setPagamentosPlanos(Set<PagamentoPlano> pagamentosPlanos) {
        this.pagamentosPlanos = pagamentosPlanos;
    }

/*    public Set<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(Set<Produto> produtos) {
        this.produtos = produtos;
    }*/

    public Set<Servico> getServicos() {
        return servicos;
    }

    public void setServicos(Set<Servico> servicos) {
        this.servicos = servicos;
    }

    public Set<Carrinho> getCarrinhos() {
        return carrinhos;
    }

    public void setCarrinhos(Set<Carrinho> carrinhos) {
        this.carrinhos = carrinhos;
    }

    public Set<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(Set<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public Set<PagamentoProduto> getPagamentosProdutos() {
        return pagamentosProdutos;
    }

    public void setPagamentosProdutos(Set<PagamentoProduto> pagamentosProdutos) {
        this.pagamentosProdutos = pagamentosProdutos;
    }

    public Set<PagamentoServico> getPagamentosServicos() {
        return pagamentosServicos;
    }

    public void setPagamentosServicos(Set<PagamentoServico> pagamentosServicos) {
        this.pagamentosServicos = pagamentosServicos;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "uid='" + uid + '\'' +
                ", cpf='" + cpf + '\'' +
                ", nomeCompleto='" + nomeCompleto + '\'' +
                ", nomeUsuario='" + nomeUsuario + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", telefone='" + telefone + '\'' +
                ", genero=" + genero +
                ", dataNascimento=" + dataNascimento +
                ", enderecos=" + enderecos +
                ", planosUsuarios=" + planosUsuarios +
                ", pagamentosPlanos=" + pagamentosPlanos +
                ", produtos="/* + produtos*/ +
                ", servicos=" + servicos +
                ", carrinhos=" + carrinhos +
                ", pedidos=" + pedidos +
                ", pagamentosProdutos=" + pagamentosProdutos +
                ", pagamentosServicos=" + pagamentosServicos +
                '}';
    }

}
