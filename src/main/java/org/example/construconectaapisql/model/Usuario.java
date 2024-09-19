package org.example.construconectaapisql.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Date;
import java.util.Set;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Long usuarioId;

    @CPF(message = "CPF Inválido!")
    @Column(nullable = false, unique = true)
    @Size(min = 11, max = 11, message = "O CPF deve ter exatamente 11 caracteres")
    private String cpf;

    @Column(name = "nome_completo", nullable = false)
    @Size(min = 6, max = 300, message = "O nome deve ter no mínimo 6 e no máximo 300 caracteres")
    private String nomeCompleto;

    @Column(name = "nome_usuario", nullable = false, unique = true)
    @Size(min = 4, max = 20, message = "O nome de usuário deve ter no mínimo 4 e no máximo 20 caracteres")
    private String nomeUsuario;

    @Column(nullable = false, unique = true)
    @Size(max = 250, message = "O e-mail deve ter no máximo 250 caracteres.")
    private String email;

    @NotNull
    @Size(min = 8, max = 500, message = "A senha deve ter no mínimo 8 e no máximo 500 caracteres")
    private String senha;

    @NotNull
    @Size(min = 10, max = 15, message = "O telefone deve ter entre 10 e 15 caracteres")
    private String telefone;

    @NotNull
    private Integer genero;

    @Column(name = "data_nascimento", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;

    @OneToMany(mappedBy = "usuario")
    private Set<EnderecoUsuario> enderecos;

    @OneToMany(mappedBy = "usuario")
    private Set<PlanoUsuario> planosUsuarios;

    @OneToMany(mappedBy = "usuario")
    private Set<PagamentoPlano> pagamentosPlanos;

    @OneToMany(mappedBy = "usuario")
    private Set<Produto> produtos;

    @OneToMany(mappedBy = "usuario")
    private Set<Servico> servicos;

    @OneToMany(mappedBy = "usuario")
    private Set<Carrinho> carrinhos;

    @OneToMany(mappedBy = "usuario")
    private Set<Pedido> pedidos;

    @OneToMany(mappedBy = "usuario")
    private Set<PagamentoProduto> pagamentosProdutos;

    @OneToMany(mappedBy = "usuario")
    private Set<PagamentoServico> pagamentosServicos;

    // Constructors, Getters and Setters
    public Usuario() {}

    public Usuario(
            Long usuarioId,
            String cpf,
            String nomeCompleto,
            String nomeUsuario,
            String email,
            String senha,
            String telefone,
            Integer genero,
            Date dataNascimento
    ) {
        this.usuarioId = usuarioId;
        this.cpf = cpf;
        this.nomeCompleto = nomeCompleto;
        this.nomeUsuario = nomeUsuario;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.genero = genero;
        this.dataNascimento = dataNascimento;
    }

    // Getters and Setters
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }

    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public Integer getGenero() { return genero; }
    public void setGenero(Integer genero) { this.genero = genero; }

    public Date getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(Date dataNascimento) { this.dataNascimento = dataNascimento; }

    public Set<EnderecoUsuario> getEnderecos() { return enderecos; }
    public void setEnderecos(Set<EnderecoUsuario> enderecos) { this.enderecos = enderecos; }

    public Set<PlanoUsuario> getPlanosUsuarios() { return planosUsuarios; }
    public void setPlanosUsuarios(Set<PlanoUsuario> planosUsuarios) { this.planosUsuarios = planosUsuarios; }

    public Set<PagamentoPlano> getPagamentosPlanos() { return pagamentosPlanos; }
    public void setPagamentosPlanos(Set<PagamentoPlano> pagamentosPlanos) { this.pagamentosPlanos = pagamentosPlanos; }

    public Set<Produto> getProdutos() { return produtos; }
    public void setProdutos(Set<Produto> produtos) { this.produtos = produtos; }

    public Set<Servico> getServicos() { return servicos; }
    public void setServicos(Set<Servico> servicos) { this.servicos = servicos; }

    public Set<Carrinho> getCarrinhos() { return carrinhos; }
    public void setCarrinhos(Set<Carrinho> carrinhos) { this.carrinhos = carrinhos; }

    public Set<Pedido> getPedidos() { return pedidos; }
    public void setPedidos(Set<Pedido> pedidos) { this.pedidos = pedidos; }

    public Set<PagamentoProduto> getPagamentosProdutos() { return pagamentosProdutos; }
    public void setPagamentosProdutos(Set<PagamentoProduto> pagamentosProdutos) { this.pagamentosProdutos = pagamentosProdutos; }

    public Set<PagamentoServico> getPagamentosServicos() { return pagamentosServicos; }
    public void setPagamentosServicos(Set<PagamentoServico> pagamentosServicos) { this.pagamentosServicos = pagamentosServicos; }
}
