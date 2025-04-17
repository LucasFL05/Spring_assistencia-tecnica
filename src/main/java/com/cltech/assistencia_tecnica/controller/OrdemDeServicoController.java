package com.cltech.assistencia_tecnica.controller;

import com.cltech.assistencia_tecnica.dto.OrdemDeServicoDTO;
import com.cltech.assistencia_tecnica.model.OrdemDeServico;
import com.cltech.assistencia_tecnica.service.ClienteService;
import com.cltech.assistencia_tecnica.service.DispositivoService;
import com.cltech.assistencia_tecnica.service.OrdemDeServicoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ordens")
public class OrdemDeServicoController {

    @Autowired
    private OrdemDeServicoService ordemDeServicoService;

    @Autowired
    private DispositivoService dispositivoService;

    @Autowired
    private ClienteService clienteService;


    @PostMapping
    @Operation(summary = "Create a new ordem de serviço")
    public ResponseEntity<OrdemDeServicoDTO> criarOrdemDeServico(@Valid @RequestBody OrdemDeServicoDTO ordemDeServicoDTO) {
        OrdemDeServico ordemDeServico = convertToEntity(ordemDeServicoDTO);
        OrdemDeServico novaOrdem = ordemDeServicoService.criarOrdemDeServico(ordemDeServico);
        return ResponseEntity.ok(convertToDTO(novaOrdem));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an ordem de serviço by ID")
    public ResponseEntity<OrdemDeServicoDTO> buscarOrdemPorId(@PathVariable Long id) {
        OrdemDeServico ordem = ordemDeServicoService.buscarOrdemPorId(id);
        return ResponseEntity.ok(convertToDTO(ordem));
    }

    @GetMapping
    @Operation(summary = "List all ordens de serviço")
    public ResponseEntity<List<OrdemDeServicoDTO>> listarOrdens() {
        List<OrdemDeServico> ordens = ordemDeServicoService.listarOrdens();
        List<OrdemDeServicoDTO> ordensDTO = ordens.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(ordensDTO);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update the status of an ordem de serviço by ID")
    public ResponseEntity<OrdemDeServicoDTO> atualizarStatus(@PathVariable Long id, @RequestBody String novoStatus) {
        OrdemDeServico ordemAtualizada = ordemDeServicoService.atualizarStatus(id, novoStatus);
        return ResponseEntity.ok(convertToDTO(ordemAtualizada));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an ordem de serviço by ID")
    public ResponseEntity<Void> deletarOrdem(@PathVariable Long id) {
        ordemDeServicoService.deletarOrdem(id);
        return ResponseEntity.noContent().build();
    }
}