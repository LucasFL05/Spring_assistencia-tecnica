package com.cltech.assistencia_tecnica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class OrdemDeServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A descrição do problema é obrigatória")
    private String descricaoProblema;

    @NotBlank(message = "O status da ordem de serviço é obrigatório")
    private String status; // Ex.: "Aberta", "Em andamento", "Concluída"

    @NotNull(message = "A data de abertura não pode ser nula")
    private LocalDateTime dataAbertura = LocalDateTime.now();

    private LocalDateTime dataConclusao;

    @ManyToOne
    @JoinColumn(name = "dispositivo_id")
    @NotNull(message = "Um dispositivo deve estar associado à ordem de serviço")
    private Dispositivo dispositivo;
}
