package com.cltech.assistencia_tecnica.controller;

import com.cltech.assistencia_tecnica.model.Dispositivo;
import com.cltech.assistencia_tecnica.service.DispositivoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dispositivos")
public class DispositivoController {

    @Autowired
    private DispositivoService dispositivoService;

    @PostMapping
    public ResponseEntity<Dispositivo> criarDispositivo(@Valid @RequestBody Dispositivo dispositivo) {
        Dispositivo novoDispositivo = dispositivoService.criarDispositivo(dispositivo);
        return ResponseEntity.ok(novoDispositivo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dispositivo> buscarDispositivoPorId(@PathVariable Long id) {
        Dispositivo dispositivo = dispositivoService.buscarDispositivoPorId(id);
        return ResponseEntity.ok(dispositivo);
    }

    @GetMapping
    public ResponseEntity<List<Dispositivo>> listarDispositivos() {
        List<Dispositivo> dispositivos = dispositivoService.listarDispositivos();
        return ResponseEntity.ok(dispositivos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDispositivo(@PathVariable Long id) {
        dispositivoService.deletarDispositivo(id);
        return ResponseEntity.noContent().build();
    }
}
