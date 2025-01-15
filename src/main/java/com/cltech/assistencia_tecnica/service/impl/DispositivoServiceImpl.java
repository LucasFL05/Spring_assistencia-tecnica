package com.cltech.assistencia_tecnica.service.impl;

import com.cltech.assistencia_tecnica.model.Dispositivo;
import com.cltech.assistencia_tecnica.repository.DispositivoRepository;
import com.cltech.assistencia_tecnica.service.DispositivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DispositivoServiceImpl implements DispositivoService {

    @Autowired
    private DispositivoRepository dispositivoRepository;

    @Override
    public Dispositivo criarDispositivo(Dispositivo dispositivo) {
        return dispositivoRepository.save(dispositivo);
    }

    @Override
    public Dispositivo buscarDispositivoPorId(Long id) {
        return dispositivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispositivo não encontrado com o ID: " + id));
    }

    @Override
    public List<Dispositivo> listarDispositivos() {
        return dispositivoRepository.findAll();
    }

    @Override
    public void deletarDispositivo(Long id) {
        Dispositivo dispositivo = buscarDispositivoPorId(id);
        dispositivoRepository.delete(dispositivo);
    }
}
