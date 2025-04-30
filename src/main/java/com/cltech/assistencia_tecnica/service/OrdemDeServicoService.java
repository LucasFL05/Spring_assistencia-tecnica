package com.cltech.assistencia_tecnica.service;

import com.cltech.assistencia_tecnica.dto.OrdemDeServicoDTO;

import java.util.List;

public interface OrdemDeServicoService {
    OrdemDeServicoDTO criarOrdemDeServico(OrdemDeServicoDTO ordemDeServicoDTO);
    OrdemDeServicoDTO buscarOrdemPorId(Long id);
    List<OrdemDeServicoDTO> listarOrdens();
    OrdemDeServicoDTO atualizarStatus(Long id, String novoStatus);
    void deletarOrdem(Long id);
}
