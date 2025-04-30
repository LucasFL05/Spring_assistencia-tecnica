package com.cltech.assistencia_tecnica.controller;

import com.cltech.assistencia_tecnica.dto.DispositivoDTO;
import com.cltech.assistencia_tecnica.service.DispositivoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dispositivos")
@Tag(name = "Dispositivo", description = "Endpoints for managing dispositivos")
public class DispositivoController {

    private final DispositivoService dispositivoService;

    public DispositivoController(DispositivoService dispositivoService) {
        this.dispositivoService = dispositivoService;
    }

    @PostMapping
    @Operation(
            summary = "Create a new dispositivo",
            description = "Creates a new dispositivo and returns it",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Dispositivo created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    public ResponseEntity<DispositivoDTO> criarDispositivo(@Valid @RequestBody DispositivoDTO dispositivoDTO) {
        DispositivoDTO novoDispositivo = dispositivoService.criarDispositivo(dispositivoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoDispositivo);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a dispositivo by ID")
    public ResponseEntity<DispositivoDTO> buscarDispositivoPorId(@PathVariable Long id) {
        try {
            DispositivoDTO dispositivo = dispositivoService.buscarDispositivoPorId(id);
            return ResponseEntity.ok(dispositivo);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a dispositivo by ID")
    public ResponseEntity<DispositivoDTO> atualizarDispositivo(
            @PathVariable Long id, @Valid @RequestBody DispositivoDTO dispositivoDTO) {
        if (!id.equals(dispositivoDTO.getId())) {
            return ResponseEntity.badRequest().build();
        }
        DispositivoDTO dispositivoAtualizado = dispositivoService.atualizarDispositivo(id, dispositivoDTO);
        return dispositivoAtualizado != null ? ResponseEntity.ok(dispositivoAtualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a dispositivo by ID")
    public ResponseEntity<Void> deletarDispositivo(@PathVariable Long id) {
        try {
            dispositivoService.deletarDispositivo(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(summary = "List all dispositivos")
    public ResponseEntity<List<DispositivoDTO>> listarDispositivos() {
        List<DispositivoDTO> dispositivos = dispositivoService.listarDispositivos();
        return ResponseEntity.ok(dispositivos);
    }
}
