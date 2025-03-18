package com.cltech.assistencia_tecnica.controller;

import com.cltech.assistencia_tecnica.dto.DispositivoDTO;
import com.cltech.assistencia_tecnica.service.DispositivoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dispositivos")
@Tag(name = "Dispositivo", description = "Endpoints for managing dispositivos")
public class DispositivoController {

    @Autowired
    private DispositivoService dispositivoService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    @Operation(summary = "Create a new dispositivo")
    public ResponseEntity<DispositivoDTO> criarDispositivo(@Valid @RequestBody DispositivoDTO dispositivoDTO) {
        DispositivoDTO novoDispositivo = dispositivoService.criarDispositivo(dispositivoDTO);
        return ResponseEntity.ok(novoDispositivo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a dispositivo by ID")
    public ResponseEntity<DispositivoDTO> buscarDispositivoPorId(@PathVariable Long id) {
        DispositivoDTO dispositivo = dispositivoService.buscarDispositivoPorId(id);
        return ResponseEntity.ok(dispositivo);
    }

    @GetMapping
    @Operation(summary = "List all dispositivos")
    public ResponseEntity<List<DispositivoDTO>> listarDispositivos() {
        List<DispositivoDTO> dispositivos = dispositivoService.listarDispositivos();
        return ResponseEntity.ok(dispositivos);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a dispositivo by ID")
    public ResponseEntity<DispositivoDTO> atualizarDispositivo(
            @PathVariable Long id, @Valid @RequestBody DispositivoDTO dispositivoDTO) {
        DispositivoDTO dispositivoAtualizado = dispositivoService.atualizarDispositivo(id, dispositivoDTO);
        return ResponseEntity.ok(dispositivoAtualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a dispositivo by ID")
    public ResponseEntity<Void> deletarDispositivo(@PathVariable Long id) {
        dispositivoService.deletarDispositivo(id);
        return ResponseEntity.noContent().build();
    }
}