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

    private String descricaoProblema;

    private String status; // Ex.: "Aberta", "Em andamento", "Concluída"

    private LocalDateTime dataAbertura = LocalDateTime.now();

    private LocalDateTime dataConclusao;

    @ManyToOne
    @JoinColumn(name = "dispositivo_id")
    private Dispositivo dispositivo;
}
