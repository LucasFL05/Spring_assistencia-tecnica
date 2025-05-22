package com.cltech.assistencia_tecnica.dto;

import java.time.LocalDateTime;

public record OrdemResumoDTO(
        Long id,
        String cliente,
        String dispositivo,
        String status,
        LocalDateTime dataAbertura
) {}
