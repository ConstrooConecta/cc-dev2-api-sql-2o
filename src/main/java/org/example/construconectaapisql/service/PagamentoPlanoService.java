package org.example.construconectaapisql.service;

import org.example.construconectaapisql.model.PagamentoPlano;
import org.example.construconectaapisql.repository.PagamentoPlanoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PagamentoPlanoService {
    private final PagamentoPlanoRepository pagamentoPlanoRepository;

    public PagamentoPlanoService(
            PagamentoPlanoRepository pagamentoPlanoRepository
    ) {
        this.pagamentoPlanoRepository = pagamentoPlanoRepository;
    }

    public List<PagamentoPlano> findAllPaymentsPlan() { return pagamentoPlanoRepository.findAll(); }

    public PagamentoPlano savePaymentPlan(PagamentoPlano pagamentoPlano) { return pagamentoPlanoRepository.save(pagamentoPlano); }

    @Transactional
    public PagamentoPlano deletePaymentPlan(Long pagamentoPlanoId) {
        PagamentoPlano pagamentoPlano = findPaymentPlanById(pagamentoPlanoId);
        pagamentoPlanoRepository.delete(pagamentoPlano);
        return pagamentoPlano;
    }

    public PagamentoPlano findPaymentPlanById(Long id) {
        return pagamentoPlanoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dados de Pagamento do Plano não encontrado."));
    }

    public List<PagamentoPlano> findByPlanoId(Integer plano) { return pagamentoPlanoRepository.findByPlano(plano); }
    public List<PagamentoPlano> findByUserId(String usuario) { return pagamentoPlanoRepository.findByUsuario(usuario); }
    public List<PagamentoPlano> findByDataPagamento(Date dataPagamento) { return pagamentoPlanoRepository.findByDataPagamento(dataPagamento); }
    public List<PagamentoPlano> findByTipoPagamento(String tipoPagamento) { return pagamentoPlanoRepository.findByTipoPagamento(tipoPagamento); }

}
