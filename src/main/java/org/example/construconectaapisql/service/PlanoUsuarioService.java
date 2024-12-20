package org.example.construconectaapisql.service;

import org.example.construconectaapisql.model.PlanoUsuario;
import org.example.construconectaapisql.repository.PlanoUsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlanoUsuarioService {
    private final PlanoUsuarioRepository planoUsuarioRepository;

    public PlanoUsuarioService(
            PlanoUsuarioRepository planoUsuarioRepository
    ) {
        this.planoUsuarioRepository = planoUsuarioRepository;
    }

    // crud methods
    public List<PlanoUsuario> findAllUserPlans() {
        return planoUsuarioRepository.findAll();
    }

    public PlanoUsuario saveUserPlan(PlanoUsuario planoUsuario) {
        return planoUsuarioRepository.save(planoUsuario);
    }

    @Transactional
    public PlanoUsuario deleteUserPlan(Long planoUsuarioId) {
        PlanoUsuario planoUsuario = findUserPlanById(planoUsuarioId);
        planoUsuarioRepository.delete(planoUsuario);
        return planoUsuario;
    }

    public PlanoUsuario findUserPlanById(Long planoUsuarioId) {
        return planoUsuarioRepository.findById(planoUsuarioId)
                .orElseThrow(() -> new RuntimeException("Plano do Usuário não encontrado."));
    }

    // others methods
    public List<PlanoUsuario> findUserPlansByUserId(String usuario) {
        return planoUsuarioRepository.findByUsuario(usuario);
    }

    public List<PlanoUsuario> findUserPlanByPlanoId(Integer plano) {
        return planoUsuarioRepository.findByPlano(plano);
    }

    public List<PlanoUsuario> findUserPlanByDataAssinatura(String dataAssinatura) {
        return planoUsuarioRepository.findByDataAssinatura(dataAssinatura);
    }

    public List<PlanoUsuario> findUserPlanByDataFinal(String dataFinal) {
        return planoUsuarioRepository.findByDataFinal(dataFinal);
    }

}
