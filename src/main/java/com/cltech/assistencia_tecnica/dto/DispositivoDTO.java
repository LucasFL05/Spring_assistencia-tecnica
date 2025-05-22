package com.cltech.assistencia_tecnica.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DispositivoDTO {

    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            description = "ID único do dispositivo",
            example = "1"
    )
    private Long id;

    @NotBlank(message = "O tipo do dispositivo é obrigatório")
    @Size(max = 50, message = "O tipo deve ter no máximo 50 caracteres")
    @Schema(
            description = "Tipo do dispositivo",
            example = "Notebook"
    )
    private String tipo;

    @NotBlank(message = "A marca do dispositivo é obrigatória")
    @Size(max = 50, message = "A marca deve ter no máximo 50 caracteres")
    @Schema(
            description = "Marca do dispositivo",
            example = "Dell"
    )
    private String marca;

    @NotBlank(message = "O modelo do dispositivo é obrigatório")
    @Size(max = 50, message = "O modelo deve ter no máximo 50 caracteres")
    @Schema(
            description = "Modelo do dispositivo",
            example = "Inspiron 15"
    )
    private String modelo;

    @NotNull(message = "O ID do cliente é obrigatório")
    @Schema(
            description = "ID do cliente proprietário deste dispositivo",
            example = "5"
    )
    private Long clienteId;
}
