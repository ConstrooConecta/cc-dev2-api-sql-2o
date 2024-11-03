package org.example.construconectaapisql.service;

import org.example.construconectaapisql.model.Plano;
import org.example.construconectaapisql.repository.PlanoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlanoService {

    private final PlanoRepository planoRepository;

    public PlanoService(
            PlanoRepository planoRepository
    ) {
        this.planoRepository = planoRepository;
    }

    // Retorna todos os planos cadastrados
    public List<Plano> findAllPlans() {
        return planoRepository.findAll(Sort.by(Sort.Direction.ASC, "planoId"));
    }

    // Salva um novo plano com validação de campos únicos
    @Transactional
    public Plano savePlan(Plano plano) {
        boolean isUpdate = plano.getPlanoId() != null && planoRepository.existsById(plano.getPlanoId());
        validateUniqueFields(plano, isUpdate); // Validação de nome único
        return planoRepository.save(plano);
    }

    // Busca um plano por ID
    public Plano findPlanById(Long planoId) {
        return planoRepository.findById(planoId)
                .orElseThrow(() -> new RuntimeException("Plano não encontrado."));
    }

    public List<Plano> findByNomeCompletoLikeIgnoreCase(String nome) {
        return planoRepository.findByNomeLikeIgnoreCase(nome);
    }

    // Deleta um plano pelo ID
    @Transactional
    public Plano deletePlan(Long planoId) {
        Plano plano = findPlanById(planoId);
        planoRepository.delete(plano);
        return plano;
    }

    // Método para validar se o nome do plano é único
    private void validateUniqueFields(Plano plano, boolean isUpdate) {
        if (!isUpdate || !planoRepository.findById(plano.getPlanoId()).get().getNome().equals(plano.getNome())) {
            if (!planoRepository.findByNomeLikeIgnoreCase(plano.getNome()).isEmpty()) {
                throw new RuntimeException("Nome do plano já está em uso.");
            }
        }
    }
}

