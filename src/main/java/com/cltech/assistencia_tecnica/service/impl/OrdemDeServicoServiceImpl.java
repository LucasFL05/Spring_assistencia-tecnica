package com.cltech.assistencia_tecnica.service.impl;

import com.cltech.assistencia_tecnica.dto.OrdemDeServicoDTO;
import com.cltech.assistencia_tecnica.mapper.OrdemDeServicoMapper;
import com.cltech.assistencia_tecnica.model.OrdemDeServico;
import com.cltech.assistencia_tecnica.model.StatusOrdemServico;
import com.cltech.assistencia_tecnica.repository.OrdemDeServicoRepository;
import com.cltech.assistencia_tecnica.service.DispositivoService;
import com.cltech.assistencia_tecnica.service.OrdemDeServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdemDeServicoServiceImpl implements OrdemDeServicoService {

    @Autowired
    private OrdemDeServicoRepository ordemDeServicoRepository;

    @Autowired
    private OrdemDeServicoMapper ordemDeServicoMapper;

    @Autowired
    private DispositivoService dispositivoService;

    @Override
    public OrdemDeServicoDTO criarOrdemDeServico(OrdemDeServicoDTO dto) {
        OrdemDeServico entity = ordemDeServicoMapper.toEntity(dto);

        entity.setDispositivo(dispositivoService.buscarEntidadeDispositivoPorId(dto.getDispositivoId()));

        OrdemDeServico salvo = ordemDeServicoRepository.save(entity);
        return ordemDeServicoMapper.toDto(salvo);
    }

    @Override
    public OrdemDeServicoDTO buscarOrdemPorId(Long id) {
        OrdemDeServico entity = ordemDeServicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ordem de Serviço não encontrada com o ID: " + id));
        return ordemDeServicoMapper.toDto(entity);
    }

    @Override
    public List<OrdemDeServicoDTO> listarOrdens() {
        return ordemDeServicoRepository.findAll()
                .stream()
                .map(ordemDeServicoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrdemDeServicoDTO atualizarStatus(Long id, String novoStatus) {
        OrdemDeServico ordem = ordemDeServicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ordem de Serviço não encontrada com o ID: " + id));

        try {
            ordem.setStatus(StatusOrdemServico.valueOf(novoStatus.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status inválido: " + novoStatus);
        }

        return ordemDeServicoMapper.toDto(ordemDeServicoRepository.save(ordem));
    }

    @Override
    public void deletarOrdem(Long id) {
        if (!ordemDeServicoRepository.existsById(id)) {
            throw new RuntimeException("Ordem de Serviço não encontrada para exclusão com ID: " + id);
        }
        ordemDeServicoRepository.deleteById(id);
    }
}