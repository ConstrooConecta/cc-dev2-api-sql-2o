package org.example.construconectaapisql.service;

import org.example.construconectaapisql.model.PagamentoPlano;
import org.example.construconectaapisql.model.Plano;
import org.example.construconectaapisql.repository.PagamentoPlanoRepository;
import org.example.construconectaapisql.repository.PlanoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PagamentoPlanoService {
    private final PagamentoPlanoRepository pagamentoPlanoRepository;
    private final PlanoRepository planoRepository; // Adicionando um repositório de Plano

    public PagamentoPlanoService(PagamentoPlanoRepository pagamentoPlanoRepository, PlanoRepository planoRepository) {
        this.pagamentoPlanoRepository = pagamentoPlanoRepository;
        this.planoRepository = planoRepository; // Injetando o repositório de Plano
    }

    public List<PagamentoPlano> findAllPaymentsPlan() {
        return pagamentoPlanoRepository.findAll();
    }

    public PagamentoPlano savePaymentPlan(PagamentoPlano pagamentoPlano) {
        // Buscando o plano associado pelo ID e garantindo que o valor do pagamento seja o valor do plano
        Plano plano = planoRepository.findById(Long.valueOf(pagamentoPlano.getPlano()))
                .orElseThrow(() -> new RuntimeException("Plano não encontrado para o ID fornecido"));

        // Definindo o valor do pagamento igual ao valor do plano
        pagamentoPlano.setValor(plano.getValor());

        return pagamentoPlanoRepository.save(pagamentoPlano);
    }

    @Transactional
    public PagamentoPlano deletePaymentPlan(Long pagamentoPlanoId) {
        PagamentoPlano pagamentoPlano = findPaymentPlanById(pagamentoPlanoId);
        pagamentoPlanoRepository.delete(pagamentoPlano);
        return pagamentoPlano;
    }

    public PagamentoPlano findPaymentPlanById(Long id) {
        return pagamentoPlanoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dados de Pagamento do Plano não encontrado.")); }

    public List<PagamentoPlano> findByPlanoId(Integer plano) { return pagamentoPlanoRepository.findByPlano(plano); }
    public List<PagamentoPlano> findByUserId(String usuario) { return pagamentoPlanoRepository.findByUsuario(usuario); }
    public List<PagamentoPlano> findByDataPagamento(Date dataPagamento) { return pagamentoPlanoRepository.findByDataPagamento(dataPagamento); }
    public List<PagamentoPlano> findByTipoPagamento(String tipoPagamento) { return pagamentoPlanoRepository.findByTipoPagamento(tipoPagamento); }
}
