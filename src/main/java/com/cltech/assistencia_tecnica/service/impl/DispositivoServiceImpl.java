package com.cltech.assistencia_tecnica.service.impl;

import com.cltech.assistencia_tecnica.dto.DispositivoDTO;
import com.cltech.assistencia_tecnica.exception.base.ConflitoDeDadosException;
import com.cltech.assistencia_tecnica.exception.base.EntidadeNaoEncontradaException;
import com.cltech.assistencia_tecnica.mapper.DispositivoMapper;
import com.cltech.assistencia_tecnica.model.Cliente;
import com.cltech.assistencia_tecnica.model.Dispositivo;
import com.cltech.assistencia_tecnica.repository.DispositivoRepository;
import com.cltech.assistencia_tecnica.service.ClienteService;
import com.cltech.assistencia_tecnica.service.DispositivoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DispositivoServiceImpl implements DispositivoService {

    private final DispositivoRepository dispositivoRepository;
    private final ClienteService clienteService;
    private final DispositivoMapper dispositivoMapper;

    @Override
    public DispositivoDTO criarDispositivo(DispositivoDTO dto) {
        Dispositivo entidade = dispositivoMapper.dispositivoDTOToDispositivo(dto);

        Cliente cliente = clienteService.buscarEntidadePorId(dto.getClienteId());
        entidade.setCliente(cliente);

        Dispositivo salvo = dispositivoRepository.save(entidade);
        return dispositivoMapper.dispositivoToDispositivoDTO(salvo);
    }

    @Override
    public DispositivoDTO buscarDispositivoPorId(Long id) {
        Dispositivo dispositivo = dispositivoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Dispositivo não encontrado com o ID: " + id));
        return dispositivoMapper.dispositivoToDispositivoDTO(dispositivo);
    }

    @Override
    public List<DispositivoDTO> listarDispositivos() {
        return dispositivoRepository.findAll().stream()
                .map(dispositivoMapper::dispositivoToDispositivoDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DispositivoDTO atualizarDispositivo(Long id, DispositivoDTO dto) {
        Dispositivo existente = dispositivoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Dispositivo não encontrado com o ID: " + id));

        Dispositivo atualizado = dispositivoMapper.dispositivoDTOToDispositivo(dto);
        atualizado.setId(existente.getId());

        Cliente cliente = clienteService.buscarEntidadePorId(dto.getClienteId());
        atualizado.setCliente(cliente);

        Dispositivo salvo = dispositivoRepository.save(atualizado);
        return dispositivoMapper.dispositivoToDispositivoDTO(salvo);
    }

    @Override
    public void deletarDispositivo(Long id) {
        Dispositivo dispositivo = dispositivoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Dispositivo não encontrado com o ID: " + id));

        if (!dispositivo.getOrdensDeServico().isEmpty()) {
            throw new ConflitoDeDadosException(
                    "Não é possível excluir o dispositivo com ID " + id +
                            " pois existem ordens de serviço associadas.");
        }

        try {
            dispositivoRepository.delete(dispositivo);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflitoDeDadosException(
                    "Falha ao excluir o dispositivo com ID " + id +
                            ": violação de integridade de dados.");
        }
    }

    @Override
    public Dispositivo buscarEntidadeDispositivoPorId(Long id) {
        return dispositivoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Dispositivo não encontrado com o ID: " + id));
    }
}
