package com.cltech.assistencia_tecnica.service;

import com.cltech.assistencia_tecnica.model.OrdemDeServico;

import java.util.List;

public interface OrdemDeServicoService {
    OrdemDeServico criarOrdemDeServico(OrdemDeServico ordemDeServico);
    OrdemDeServico buscarOrdemPorId(Long id);
    List<OrdemDeServico> listarOrdens();
    OrdemDeServico atualizarStatus(Long id, String novoStatus);
    void deletarOrdem(Long id);
}