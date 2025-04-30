package com.cltech.assistencia_tecnica.service.impl;

import com.cltech.assistencia_tecnica.dto.DispositivoDTO;
import com.cltech.assistencia_tecnica.model.Dispositivo;
import com.cltech.assistencia_tecnica.repository.DispositivoRepository;
import com.cltech.assistencia_tecnica.service.ClienteService;
import com.cltech.assistencia_tecnica.service.DispositivoService;
import com.cltech.assistencia_tecnica.mapper.DispositivoMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DispositivoServiceImpl implements DispositivoService {

    @Autowired
    private DispositivoRepository dispositivoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private DispositivoMapper dispositivoMapper;

    @Override
    public DispositivoDTO criarDispositivo(DispositivoDTO dispositivoDTO) {
        Dispositivo dispositivo = dispositivoMapper.dispositivoDTOToDispositivo(dispositivoDTO);
        Dispositivo novoDispositivo = dispositivoRepository.save(dispositivo);
        return dispositivoMapper.dispositivoToDispositivoDTO(novoDispositivo);
    }

    @Override
    public DispositivoDTO buscarDispositivoPorId(Long id) {
        Dispositivo dispositivo = dispositivoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dispositivo não encontrado com o ID: " + id));
        return dispositivoMapper.dispositivoToDispositivoDTO(dispositivo);
    }

    @Override
    public List<DispositivoDTO> listarDispositivos() {
        List<Dispositivo> dispositivos = dispositivoRepository.findAll();
        return dispositivos.stream()
                .map(dispositivoMapper::dispositivoToDispositivoDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DispositivoDTO atualizarDispositivo(Long id, DispositivoDTO dispositivoDTO) {
        Dispositivo dispositivoExistente = dispositivoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dispositivo não encontrado com o ID: " + id));
        Dispositivo dispositivoAtualizado = dispositivoMapper.dispositivoDTOToDispositivo(dispositivoDTO);
        dispositivoAtualizado.setId(dispositivoExistente.getId());
        dispositivoRepository.save(dispositivoAtualizado);
        return dispositivoMapper.dispositivoToDispositivoDTO(dispositivoAtualizado);
    }

    @Override
    public void deletarDispositivo(Long id) {
        Dispositivo dispositivo = dispositivoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dispositivo não encontrado com o ID: " + id));

        if (dispositivo.getOrdensDeServico().isEmpty()) {
            dispositivoRepository.delete(dispositivo);
        } else {
            throw new RuntimeException("Não é possível excluir o dispositivo, pois existem ordens de serviço associadas.");
        }
    }

    @Override
    public Dispositivo buscarEntidadeDispositivoPorId(Long id) {
        return dispositivoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dispositivo não encontrado com o ID: " + id));
    }
}
