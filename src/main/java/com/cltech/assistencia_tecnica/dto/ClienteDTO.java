package com.cltech.assistencia_tecnica.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClienteDTO {
    private Long id;

    @NotBlank(message = "O nome do cliente é obrigatório")
    @Size(max = 50, message = "O nome deve ter no máximo 50 caracteres")
    private String nome;

    @NotBlank(message = "O e-mail do cliente é obrigatório")
    @Size(max = 50, message = "O e-mail deve ter no máximo 50 caracteres")
    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank(message = "O telefone do cliente é obrigatório")
    @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
    private String telefone;
}
