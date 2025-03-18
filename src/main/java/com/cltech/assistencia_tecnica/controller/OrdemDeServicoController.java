package com.cltech.assistencia_tecnica.controller;

import com.cltech.assistencia_tecnica.dto.OrdemDeServicoDTO;
import com.cltech.assistencia_tecnica.model.OrdemDeServico;
import com.cltech.assistencia_tecnica.service.OrdemDeServicoService;
import com.cltech.assistencia_tecnica.service.DispositivoService;
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

    @PostMapping
    public ResponseEntity<OrdemDeServicoDTO> criarOrdemDeServico(@Valid @RequestBody OrdemDeServicoDTO ordemDeServicoDTO) {
        OrdemDeServico ordemDeServico = convertToEntity(ordemDeServicoDTO);
        OrdemDeServico novaOrdem = ordemDeServicoService.criarOrdemDeServico(ordemDeServico);
        return ResponseEntity.ok(convertToDTO(novaOrdem));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemDeServicoDTO> buscarOrdemPorId(@PathVariable Long id) {
        OrdemDeServico ordem = ordemDeServicoService.buscarOrdemPorId(id);
        return ResponseEntity.ok(convertToDTO(ordem));
    }

    @GetMapping
    public ResponseEntity<List<OrdemDeServicoDTO>> listarOrdens() {
        List<OrdemDeServico> ordens = ordemDeServicoService.listarOrdens();
        List<OrdemDeServicoDTO> ordensDTO = ordens.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(ordensDTO);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrdemDeServicoDTO> atualizarStatus(@PathVariable Long id, @RequestBody String novoStatus) {
        OrdemDeServico ordemAtualizada = ordemDeServicoService.atualizarStatus(id, novoStatus);
        return ResponseEntity.ok(convertToDTO(ordemAtualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarOrdem(@PathVariable Long id) {
        ordemDeServicoService.deletarOrdem(id);
        return ResponseEntity.noContent().build();
    }

    private OrdemDeServicoDTO convertToDTO(OrdemDeServico ordemDeServico) {
        OrdemDeServicoDTO dto = new OrdemDeServicoDTO();
        dto.setId(ordemDeServico.getId());
        dto.setDescricaoProblema(ordemDeServico.getDescricaoProblema());
        dto.setStatus(ordemDeServico.getStatus());
        dto.setDataAbertura(ordemDeServico.getDataAbertura());
        dto.setDataConclusao(ordemDeServico.getDataConclusao());
        dto.setDispositivoId(ordemDeServico.getDispositivo().getId());
        return dto;
    }

    private OrdemDeServico convertToEntity(OrdemDeServicoDTO dto) {
        OrdemDeServico ordemDeServico = new OrdemDeServico();
        ordemDeServico.setId(dto.getId());
        ordemDeServico.setDescricaoProblema(dto.getDescricaoProblema());
        ordemDeServico.setStatus(dto.getStatus());
        ordemDeServico.setDataAbertura(dto.getDataAbertura());
        ordemDeServico.setDataConclusao(dto.getDataConclusao());
        ordemDeServico.setDispositivo(dispositivoService.buscarDispositivoPorId(dto.getDispositivoId()).toEntity());
        return ordemDeServico;
    }
}