package com.cltech.assistencia_tecnica.controller;

import com.cltech.assistencia_tecnica.dto.OrdemDeServicoDTO;
import com.cltech.assistencia_tecnica.service.OrdemDeServicoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ordens")
public class OrdemDeServicoController {

    @Autowired
    private OrdemDeServicoService ordemDeServicoService;

    @PostMapping
    @Operation(summary = "Criar nova ordem de serviço")
    public ResponseEntity<OrdemDeServicoDTO> criarOrdemDeServico(@Valid @RequestBody OrdemDeServicoDTO dto) {
        OrdemDeServicoDTO novaOrdem = ordemDeServicoService.criarOrdemDeServico(dto);
        return ResponseEntity.ok(novaOrdem);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar ordem por ID")
    public ResponseEntity<OrdemDeServicoDTO> buscarOrdemPorId(@PathVariable Long id) {
        OrdemDeServicoDTO ordemDTO = ordemDeServicoService.buscarOrdemPorId(id);
        return ResponseEntity.ok(ordemDTO);
    }

    @GetMapping
    @Operation(summary = "Listar todas as ordens")
    public ResponseEntity<List<OrdemDeServicoDTO>> listarOrdens() {
        List<OrdemDeServicoDTO> ordens = ordemDeServicoService.listarOrdens();
        return ResponseEntity.ok(ordens);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Atualizar status da ordem")
    public ResponseEntity<OrdemDeServicoDTO> atualizarStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String novoStatus = body.get("status");
        OrdemDeServicoDTO ordemAtualizada = ordemDeServicoService.atualizarStatus(id, novoStatus);
        return ResponseEntity.ok(ordemAtualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar ordem por ID")
    public ResponseEntity<Void> deletarOrdem(@PathVariable Long id) {
        ordemDeServicoService.deletarOrdem(id);
        return ResponseEntity.noContent().build();
    }
}
