package com.cltech.assistencia_tecnica.service;

import com.cltech.assistencia_tecnica.model.Dispositivo;

import java.util.List;

public interface DispositivoService {
    Dispositivo criarDispositivo(Dispositivo dispositivo);
    Dispositivo buscarDispositivoPorId(Long id);
    List<Dispositivo> listarDispositivos();
    void deletarDispositivo(Long id);
}
