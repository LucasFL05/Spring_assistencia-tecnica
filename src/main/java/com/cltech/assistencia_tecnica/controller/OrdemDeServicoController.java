package com.cltech.assistencia_tecnica.controller;

import com.cltech.assistencia_tecnica.model.OrdemDeServico;
import com.cltech.assistencia_tecnica.service.OrdemDeServicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordens")
public class OrdemDeServicoController {

    @Autowired
    private OrdemDeServicoService ordemDeServicoService;

    @PostMapping
    public ResponseEntity<OrdemDeServico> criarOrdemDeServico(@Valid @RequestBody OrdemDeServico ordemDeServico) {
        OrdemDeServico novaOrdem = ordemDeServicoService.criarOrdemDeServico(ordemDeServico);
        return ResponseEntity.ok(novaOrdem);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemDeServico> buscarOrdemPorId(@PathVariable Long id) {
        OrdemDeServico ordem = ordemDeServicoService.buscarOrdemPorId(id);
        return ResponseEntity.ok(ordem);
    }

    @GetMapping
    public ResponseEntity<List<OrdemDeServico>> listarOrdens() {
        List<OrdemDeServico> ordens = ordemDeServicoService.listarOrdens();
        return ResponseEntity.ok(ordens);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrdemDeServico> atualizarStatus(@PathVariable Long id, @RequestBody String novoStatus) {
        OrdemDeServico ordemAtualizada = ordemDeServicoService.atualizarStatus(id, novoStatus);
        return ResponseEntity.ok(ordemAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarOrdem(@PathVariable Long id) {
        ordemDeServicoService.deletarOrdem(id);
        return ResponseEntity.noContent().build();
    }
}
