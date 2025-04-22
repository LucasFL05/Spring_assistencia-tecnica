package com.cltech.assistencia_tecnica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(
        indexes = {
                @Index(name = "idx_ordem_dispositivo_id", columnList = "dispositivo_id")
        }
)
public class OrdemDeServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A descrição do problema é obrigatória.")
    @Column(nullable = false, length = 500)
    private String descricaoProblema;

    @NotNull(message = "O status da ordem de serviço é obrigatório.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusOrdemServico status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataAbertura;

    private LocalDateTime dataConclusao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "dispositivo_id", nullable = false)
    private Dispositivo dispositivo;

    @PrePersist
    public void prePersist() {
        this.dataAbertura = LocalDateTime.now();
    }
}
