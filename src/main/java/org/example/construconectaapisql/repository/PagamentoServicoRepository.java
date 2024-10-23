package org.example.construconectaapisql.repository;

import org.example.construconectaapisql.model.PagamentoServico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PagamentoServicoRepository extends JpaRepository<PagamentoServico, Long> {
    Optional<PagamentoServico> findById(Long pagamentoServicoId);
    List<PagamentoServico> findByServico(Integer servico);
    List<PagamentoServico> findByUsuario(String usuario);
    List<PagamentoServico> findByTipoPagamentoLikeIgnoreCase(String tipoPagamento);
    List<PagamentoServico> findByDataPagamento(Date dataPagamento);
}
