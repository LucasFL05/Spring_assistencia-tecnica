package com.cltech.assistencia_tecnica.controller;

import com.cltech.assistencia_tecnica.dto.DispositivoDTO;
import com.cltech.assistencia_tecnica.service.DispositivoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
            summary     = "Create a new dispositivo",
            description = "Creates a new dispositivo and returns it",
            responses   = {
                    @ApiResponse(responseCode = "201", description = "Dispositivo created"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            })
    public ResponseEntity<DispositivoDTO> criarDispositivo(
            @Valid @RequestBody DispositivoDTO dispositivoDTO) {

        DispositivoDTO criado = dispositivoService.criarDispositivo(dispositivoDTO);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.getId())
                .toUri();

        return ResponseEntity
                .created(uri)
                .body(criado);
    }

    @GetMapping("/{id}")
    @Operation(
            summary   = "Get a dispositivo by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dispositivo found"),
                    @ApiResponse(responseCode = "404", description = "Dispositivo not found")
            })
    public ResponseEntity<DispositivoDTO> buscarPorId(@PathVariable Long id) {
        DispositivoDTO dto = dispositivoService.buscarDispositivoPorId(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @Operation(
            summary   = "List all dispositivos",
            responses = @ApiResponse(responseCode = "200", description = "List of dispositivos"))
    public ResponseEntity<List<DispositivoDTO>> listarTodos() {
        return ResponseEntity.ok(dispositivoService.listarDispositivos());
    }

    @PutMapping("/{id}")
    @Operation(
            summary     = "Update a dispositivo by ID",
            responses   = {
                    @ApiResponse(responseCode = "200", description = "Dispositivo updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Dispositivo not found")
            })
    public ResponseEntity<DispositivoDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody DispositivoDTO dispositivoDTO) {

        if (!id.equals(dispositivoDTO.getId())) {
            return ResponseEntity.badRequest().build();
        }

        DispositivoDTO atualizado = dispositivoService.atualizarDispositivo(id, dispositivoDTO);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary   = "Delete a dispositivo by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Dispositivo deleted"),
                    @ApiResponse(responseCode = "404", description = "Dispositivo not found")
            })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        dispositivoService.deletarDispositivo(id);
        return ResponseEntity.noContent().build();
    }
}
