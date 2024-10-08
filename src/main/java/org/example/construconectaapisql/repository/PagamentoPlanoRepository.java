package org.example.construconectaapisql.repository;

import org.example.construconectaapisql.model.PagamentoPlano;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface PagamentoPlanoRepository extends JpaRepository<PagamentoPlano, Long> {
    Optional<PagamentoPlano> findById(Long pagamentoPlanoId);
    List<PagamentoPlano> findByPlano(Integer plano);
    List<PagamentoPlano> findByUsuario(String usuario);
    List<PagamentoPlano> findByDataPagamento(Date dataPagamento);
    List<PagamentoPlano> findByTipoPagamento(String tipoPagamento);
}
