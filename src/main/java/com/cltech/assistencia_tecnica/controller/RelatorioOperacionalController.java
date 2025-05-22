package com.cltech.assistencia_tecnica.controller;

import com.cltech.assistencia_tecnica.dto.RelatorioOperacionalDTO;
import com.cltech.assistencia_tecnica.service.RelatorioOperacionalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/relatorio")
public class RelatorioOperacionalController {

    private final RelatorioOperacionalService relatorioService;

    public RelatorioOperacionalController(RelatorioOperacionalService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/operacional")
    public ResponseEntity<RelatorioOperacionalDTO> getRelatorioOperacional() {
        RelatorioOperacionalDTO relatorio = relatorioService.gerarRelatorio();
        return ResponseEntity.ok(relatorio);
    }
}
