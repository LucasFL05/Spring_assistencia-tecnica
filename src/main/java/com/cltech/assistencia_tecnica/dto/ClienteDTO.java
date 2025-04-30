package com.cltech.assistencia_tecnica.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClienteDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "ID único do cliente")
    private Long id;

    @NotBlank(message = "O nome do cliente é obrigatório")
    @Size(max = 50, message = "O nome deve ter no máximo 50 caracteres")
    @Schema(description = "Nome completo do cliente", example = "João Silva")
    private String nome;

    @NotBlank(message = "O e-mail do cliente é obrigatório")
    @Size(max = 50, message = "O e-mail deve ter no máximo 50 caracteres")
    @Email(message = "E-mail inválido")
    @Schema(description = "E-mail de contato do cliente", example = "joao.silva@email.com")
    private String email;

    @NotBlank(message = "O telefone do cliente é obrigatório")
    @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
    @Pattern(regexp = "^\\+?[0-9]{1,4}[\\s-]?[0-9]{1,4}[\\s-]?[0-9]{4,6}$", message = "Telefone inválido")
    @Schema(description = "Número de telefone do cliente", example = "+55 11 98765-4321")
    private String telefone;
}
