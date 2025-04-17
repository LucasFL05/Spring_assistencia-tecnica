package com.cltech.assistencia_tecnica.service.impl;

import com.cltech.assistencia_tecnica.dto.DispositivoDTO;
import com.cltech.assistencia_tecnica.model.Dispositivo;
import com.cltech.assistencia_tecnica.repository.DispositivoRepository;
import com.cltech.assistencia_tecnica.service.ClienteService;
import com.cltech.assistencia_tecnica.service.DispositivoService;
import org.modelmapper.ModelMapper;
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
    private ModelMapper modelMapper;

    @Override
    public DispositivoDTO criarDispositivo(DispositivoDTO dispositivoDTO) {
        Dispositivo dispositivo = convertToEntity(dispositivoDTO);
        Dispositivo novoDispositivo = dispositivoRepository.save(dispositivo);
        return convertToDTO(novoDispositivo);
    }

    @Override
    public DispositivoDTO buscarDispositivoPorId(Long id) {
        return  dispositivoRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Dispositivo não encontrado com o ID: " + id));
    }

    @Override
    public List<DispositivoDTO> listarDispositivos() {
        List<Dispositivo> dispositivos = dispositivoRepository.findAll();
        return dispositivos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DispositivoDTO atualizarDispositivo(Long id, DispositivoDTO dispositivoDTO) {
        Dispositivo dispositivoExistente = dispositivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispositivo não encontrado com o ID: " + id));
        Dispositivo dispositivoAtualizado = convertToEntity(dispositivoDTO);
        dispositivoAtualizado.setId(dispositivoExistente.getId());
        dispositivoRepository.save(dispositivoAtualizado);
        return convertToDTO(dispositivoAtualizado);
    }

    @Override
    public void deletarDispositivo(Long id) {
        Dispositivo dispositivo = dispositivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispositivo não encontrado com o ID: " + id));
        dispositivoRepository.delete(dispositivo);
    }

    private DispositivoDTO convertToDTO(Dispositivo dispositivo) {
        DispositivoDTO dto = modelMapper.map(dispositivo, DispositivoDTO.class);
        dto.setClienteId(dispositivo.getCliente().getId());
        return dto;
    }

    private Dispositivo convertToEntity(DispositivoDTO dto) {
        Dispositivo dispositivo = modelMapper.map(dto, Dispositivo.class);
        dispositivo.setCliente(clienteService.buscarClientePorId(dto.getClienteId()));
        return dispositivo;
    }
}