package com.cltech.assistencia_tecnica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrdemDeServicoDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "A descrição do problema é obrigatória")
    private String descricaoProblema;

    @NotBlank(message = "O status é obrigatório")
    @Pattern(regexp = "ABERTA|EM_ANDAMENTO|CONCLUIDA", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Status inválido. Valores aceitos: ABERTA, EM_ANDAMENTO, CONCLUIDA")
    private String status;

    @NotNull(message = "A data de abertura é obrigatória")
    private LocalDateTime dataAbertura;

    private LocalDateTime dataConclusao;

    @NotNull(message = "O dispositivo associado é obrigatório")
    private Long dispositivoId;


}
