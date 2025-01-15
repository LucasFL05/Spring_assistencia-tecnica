package com.cltech.assistencia_tecnica.service.impl;

import com.cltech.assistencia_tecnica.model.OrdemDeServico;
import com.cltech.assistencia_tecnica.repository.OrdemDeServicoRepository;
import com.cltech.assistencia_tecnica.service.OrdemDeServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdemDeServicoServiceImpl implements OrdemDeServicoService {

    @Autowired
    private OrdemDeServicoRepository ordemDeServicoRepository;

    @Override
    public OrdemDeServico criarOrdemDeServico(OrdemDeServico ordemDeServico) {
        return ordemDeServicoRepository.save(ordemDeServico);
    }

    @Override
    public OrdemDeServico buscarOrdemPorId(Long id) {
        return ordemDeServicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ordem de serviço não encontrada com o ID: " + id));
    }

    @Override
    public List<OrdemDeServico> listarOrdens() {
        return ordemDeServicoRepository.findAll();
    }

    @Override
    public OrdemDeServico atualizarStatus(Long id, String novoStatus) {
        OrdemDeServico ordem = buscarOrdemPorId(id);
        ordem.setStatus(novoStatus);
        return ordemDeServicoRepository.save(ordem);
    }

    @Override
    public void deletarOrdem(Long id) {
        OrdemDeServico ordem = buscarOrdemPorId(id);
        ordemDeServicoRepository.delete(ordem);
    }
}
