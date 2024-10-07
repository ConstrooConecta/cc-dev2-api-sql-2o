package org.example.construconectaapisql.service;

import org.example.construconectaapisql.model.Carrinho;
import org.example.construconectaapisql.repository.CarrinhoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CarrinhoService {
    private final CarrinhoRepository carrinhoRepository;

    public CarrinhoService(CarrinhoRepository carrinhoRepository) {
        this.carrinhoRepository = carrinhoRepository;
    }

    public List<Carrinho> findAllShoppingCarts() {
        return carrinhoRepository.findAll();
    }

    @Transactional
    public Carrinho saveShoppingCart(Carrinho carrinho) {
        return carrinhoRepository.save(carrinho);
    }

    public Carrinho findShoppingCartById(Long carrinhoId) {
        return carrinhoRepository.findById(carrinhoId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado."));
    }

    @Transactional
    public Carrinho deleteShoppingCart(Long carrinhoId) {
        Carrinho carrinho = findShoppingCartById(carrinhoId);
        carrinhoRepository.delete(carrinho);
        return carrinho;
    }

    public List<Carrinho> findByUserId(String usuarioId) {
        return carrinhoRepository.findByUsuario(usuarioId);
    }

    public List<Carrinho> findByProductId(Integer produtoId) {
        return carrinhoRepository.findByProduto(produtoId);
    }

}
