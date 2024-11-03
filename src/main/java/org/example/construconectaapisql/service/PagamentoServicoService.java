package org.example.construconectaapisql.service;

import org.example.construconectaapisql.model.PagamentoServico;
import org.example.construconectaapisql.repository.PagamentoServicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PagamentoServicoService {
    private final PagamentoServicoRepository pagamentoServicoRepository;

    public PagamentoServicoService(
            PagamentoServicoRepository pagamentoServicoRepository
    ) {
        this.pagamentoServicoRepository = pagamentoServicoRepository;
    }

    // crud
    public List<PagamentoServico> findAllPaymentsServices() {
        return pagamentoServicoRepository.findAll();
    }

    @Transactional
    public PagamentoServico savePaymentService(PagamentoServico pagamentoServico) {
        return pagamentoServicoRepository.save(pagamentoServico);
    }

    @Transactional
    public PagamentoServico deletePaymentService(Long pagamentoServicoId) {
        PagamentoServico pagamentoServico = findPaymentsServicesById(pagamentoServicoId);
        pagamentoServicoRepository.delete(pagamentoServico);
        return pagamentoServico;
    }

    // Other methods
    public PagamentoServico findPaymentsServicesById(Long pagamentoServicoId) {
        return pagamentoServicoRepository.findById(pagamentoServicoId)
                .orElseThrow(() -> new RuntimeException("Dados de Pagamento de Serviço não encontrado."));
    }

    public List<PagamentoServico> findByServico(Integer servico) {
        return pagamentoServicoRepository.findByServico(servico);
    }

    public List<PagamentoServico> findByUsuario(String usuario) {
        return pagamentoServicoRepository.findByUsuario(usuario);
    }

    public List<PagamentoServico> findByTipoPagamento(String tipoPagamento) {
        return pagamentoServicoRepository.findByTipoPagamentoLikeIgnoreCase(tipoPagamento);
    }

    public List<PagamentoServico> findByDataPagamento(Date dataPagamento) {
        return pagamentoServicoRepository.findByDataPagamento(dataPagamento);
    }
}
