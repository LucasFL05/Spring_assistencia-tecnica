package com.cltech.assistencia_tecnica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrdemDeServicoDTO {
    private Long id;

    @NotBlank(message = "A descrição do problema é obrigatória")
    private String descricaoProblema;

    @NotBlank(message = "O status da ordem de serviço é obrigatório")
    private String status;

    @NotNull(message = "A data de abertura não pode ser nula")
    private LocalDateTime dataAbertura;

    private LocalDateTime dataConclusao;

    @NotNull(message = "Um dispositivo deve estar associado à ordem de serviço")
    private Long dispositivoId;
}