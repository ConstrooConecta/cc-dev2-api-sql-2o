package org.example.construconectaapisql.service;

import org.springframework.transaction.annotation.Transactional;
import org.example.construconectaapisql.model.Plano;
import org.example.construconectaapisql.repository.PlanoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanoService {

    private final PlanoRepository planoRepository;

    public PlanoService(PlanoRepository planoRepository) {
        this.planoRepository = planoRepository;
    }

    // Retorna todos os planos cadastrados
    public List<Plano> findAllPlans() {
        return planoRepository.findAll();
    }

    // Salva um novo plano com validação de campos únicos
    @Transactional
    public Plano savePlan(Plano plano) {
        validateUniqueFields(plano); // Validação de nome único
        return planoRepository.save(plano);
    }

    // Busca um plano por ID
    public Plano findPlanById(Long planoId) {
        return planoRepository.findById(planoId)
                .orElseThrow(() -> new RuntimeException("Plano não encontrado."));
    }

    // Deleta um plano pelo ID
    @Transactional
    public Plano deletePlan(Long planoId) {
        Plano plano = findPlanById(planoId);
        planoRepository.delete(plano);
        return plano;
    }

    // Método para validar se o nome do plano é único
    private void validateUniqueFields(Plano plano) {
        List<Plano> existingPlans = planoRepository.findAll();
        if (existingPlans.stream().anyMatch(existingPlan ->
                existingPlan.getNome().equalsIgnoreCase(plano.getNome()))) {
            throw new RuntimeException("Um plano com este nome já existe.");
        }
    }
}

