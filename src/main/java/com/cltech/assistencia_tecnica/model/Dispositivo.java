package com.cltech.assistencia_tecnica.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Dispositivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipo;
    private String marca;
    private String modelo;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente; // Nome correto
}
