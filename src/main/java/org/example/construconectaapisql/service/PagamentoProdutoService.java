package org.example.construconectaapisql.service;

import org.example.construconectaapisql.model.PagamentoProduto;
import org.example.construconectaapisql.repository.PagamentoProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PagamentoProdutoService {
    private final PagamentoProdutoRepository pagamentoProdutoRepository;

    public PagamentoProdutoService(
            PagamentoProdutoRepository pagamentoProdutoRepository
    ) {
        this.pagamentoProdutoRepository = pagamentoProdutoRepository;
    }

    public List<PagamentoProduto> findAllProductPayment() {
        return pagamentoProdutoRepository.findAll();
    }

    @Transactional
    public PagamentoProduto saveProductPayment(PagamentoProduto pagamentoPlano) {
        return pagamentoProdutoRepository.save(pagamentoPlano);
    }

    @Transactional
    public PagamentoProduto deleteProductPayment(Long productPaymentId) {
        PagamentoProduto pagamentoProduto = findProductPaymentById(productPaymentId);
        pagamentoProdutoRepository.delete(pagamentoProduto);
        return pagamentoProduto;
    }

    public PagamentoProduto findProductPaymentById(Long productPaymentId) {
        return pagamentoProdutoRepository.findById(productPaymentId)
                .orElseThrow(() -> new RuntimeException("Dados de Pagamento de produto não encontrado."));
    }

    public List<PagamentoProduto> findProductPaymentByPedido(Integer pedido) {
        return pagamentoProdutoRepository.findByPedido(pedido);
    }

    public List<PagamentoProduto> findProductPaymentByUsuario(String usuario) {
        return pagamentoProdutoRepository.findByUsuario(usuario);
    }

    public List<PagamentoProduto> findProductPaymentByDataPagamento(Date dataPagamento) {
        return pagamentoProdutoRepository.findByDataPagamento(dataPagamento);
    }

    public List<PagamentoProduto> findProductPaymentByTipoPagamento(String tipoPagamento) {
        return pagamentoProdutoRepository.findByTipoPagamento(tipoPagamento);
    }

}
