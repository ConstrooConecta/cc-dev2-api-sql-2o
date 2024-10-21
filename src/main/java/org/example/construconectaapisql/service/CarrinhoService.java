package org.example.construconectaapisql.service;

import org.example.construconectaapisql.model.Carrinho;
import org.example.construconectaapisql.model.Produto;
import org.example.construconectaapisql.repository.CarrinhoRepository;
import org.example.construconectaapisql.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CarrinhoService {
    private final CarrinhoRepository carrinhoRepository;
    private final ProdutoRepository produtoRepository;

    @Autowired
    public CarrinhoService(
            CarrinhoRepository carrinhoRepository,
            ProdutoRepository produtoRepository
    ) {
        this.carrinhoRepository = carrinhoRepository;
        this.produtoRepository = produtoRepository;
    }

    public List<Carrinho> findAllShoppingCarts() {
        return carrinhoRepository.findAll();
    }

    @Transactional
    public Carrinho saveShoppingCart(Carrinho carrinho) {
        // Verificar se o usuário já tem um carrinho com o identificador fornecido
        List<Carrinho> carrinhosExistentes = carrinhoRepository.findByUsuario(carrinho.getUsuario());

        if (!carrinhosExistentes.isEmpty()) {
            // Se o usuário já tem um carrinho, reaproveitar o identificador
            Integer identificadorExistente = carrinhosExistentes.get(0).getIdentificador();
            if (carrinho.getIdentificador() != null && !carrinho.getIdentificador().equals(identificadorExistente)) {
                // Lógica que impede o usuário de definir um identificador diferente se já existe um
                throw new IllegalArgumentException("Usuário já possui um identificador de carrinho ativo.");
            } else {
                // Se o identificador é o mesmo ou nulo, use o existente
                carrinho.setIdentificador(identificadorExistente);
            }
        } else {
            // Se não houver carrinho, gerar um novo identificador
            Integer novoIdentificador = generateNewIdentifierForUser(carrinho.getUsuario());
            carrinho.setIdentificador(novoIdentificador);
        }

        // Buscar o produto, validar quantidade, calcular valor total, etc.
        Produto produto = produtoRepository.findById(Long.valueOf(carrinho.getProduto()))
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        // Garantir que a quantidade seja válida (maior que 0)
        if (carrinho.getQuantidade() == null || carrinho.getQuantidade() <= 0) {
            throw new IllegalArgumentException("A quantidade do produto no carrinho deve ser maior que 0.");
        }

        // Calcular o valor total (preço * desconto * quantidade)
        BigDecimal precoComDesconto = produto.getPreco().multiply(BigDecimal.ONE.subtract(produto.getDesconto()));
        BigDecimal valorTotal = precoComDesconto.multiply(new BigDecimal(carrinho.getQuantidade()));
        carrinho.setValorTotal(valorTotal);

        // Definir a imagem do produto no carrinho
        carrinho.setProdutoImg(produto.getImagem());

        return carrinhoRepository.save(carrinho);
    }

    // Método auxiliar para gerar um novo identificador (baseado na lógica do usuário)
    public Integer generateNewIdentifierForUser(String usuarioId) {
        // Caso o usuário não tenha carrinhos, gera um novo identificador
        // Neste caso, simplesmente obtemos o maior identificador existente e somamos 1
        Integer maxIdentificador = carrinhoRepository.findMaxIdentificador();
        return (maxIdentificador != null) ? maxIdentificador + 1 : 1;
    }

    public Carrinho findShoppingCartById(Long carrinhoId) {
        return carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado."));
    }

    public List<Carrinho> findByUserId(String usuarioId) {
        return carrinhoRepository.findByUsuario(usuarioId);
    }

    public List<Carrinho> findByProductId(Integer produtoId) {
        return carrinhoRepository.findByProduto(produtoId);
    }

    public List<Carrinho> findByIdentifier(Integer identificador) {
        return carrinhoRepository.findByIdentificador(identificador);
    }

    @Transactional
    public Carrinho deleteShoppingCart(Long carrinhoId) {
        Carrinho carrinho = findShoppingCartById(carrinhoId);
        carrinhoRepository.delete(carrinho);
        return carrinho;
    }

    @Transactional
    public void deleteShoppingCartByUserId(String usuario) {
        if (carrinhoRepository.findByUsuario(usuario).isEmpty()) {
            throw new RuntimeException("Nenhum carrinho encontrado para o usuario: " + usuario);
        }
        carrinhoRepository.deleteByUsuario(usuario);
    }

    @Transactional
    public void deleteShoppingCartByIdentifier(Integer identificador) {
        if (carrinhoRepository.findByIdentificador(identificador).isEmpty()) {
            throw new RuntimeException("Nenhum carrinho encontrado para o identificador: " + identificador);
        }
        carrinhoRepository.deleteByIdentificador(identificador);
    }
}

