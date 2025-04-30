package com.cltech.assistencia_tecnica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(
        indexes = {
                @Index(name = "idx_cliente_id", columnList = "cliente_id")
        }
)
public class Dispositivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50, message = "O tipo não pode ultrapassar 50 caracteres")
    @Column(nullable = false, length = 50)
    private String tipo;

    @NotBlank
    @Size(max = 50, message = "A marca não pode ultrapassar 50 caracteres")
    @Column(nullable = false, length = 50)
    private String marca;

    @NotBlank
    @Size(max = 50, message = "O modelo não pode ultrapassar 50 caracteres")
    @Column(nullable = false, length = 50)
    private String modelo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "dispositivo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdemDeServico> ordensDeServico;

    public void addOrdemDeServico(OrdemDeServico ordemDeServico) {
        ordensDeServico.add(ordemDeServico);
        ordemDeServico.setDispositivo(this);
    }

    public void removeOrdemDeServico(OrdemDeServico ordemDeServico) {
        ordensDeServico.remove(ordemDeServico);
        ordemDeServico.setDispositivo(null);
    }
}
