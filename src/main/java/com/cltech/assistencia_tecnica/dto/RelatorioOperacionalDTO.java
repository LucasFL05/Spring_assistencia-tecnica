package com.cltech.assistencia_tecnica.dto;

import java.util.List;
import java.util.Map;

public record RelatorioOperacionalDTO(
        long totalConcluidasMes,
        long totalEmAndamento,
        long totalCanceladasMes,
        long totalAbertasHoje,
        List<OrdemResumoDTO> ordensRecentes,
        Map<String, Long> distribuicaoPorStatus,
        Map<String, Long> ordensPorDia
) {}
