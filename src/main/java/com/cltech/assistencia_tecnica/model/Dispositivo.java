package com.cltech.assistencia_tecnica.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Dispositivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String tipo;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String marca;

    @NotBlank
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
