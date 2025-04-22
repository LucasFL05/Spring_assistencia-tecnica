package com.cltech.assistencia_tecnica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class OrdemDeServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A descrição do problema é obrigatória.")
    @Column(nullable = false)
    private String descricaoProblema;

    @NotNull(message = "O status da ordem de serviço é obrigatório.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusOrdemServico status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataAbertura = LocalDateTime.now();

    private LocalDateTime dataConclusao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "dispositivo_id", nullable = false)
    private Dispositivo dispositivo;
}
