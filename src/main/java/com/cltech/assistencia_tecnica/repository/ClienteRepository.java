package com.cltech.assistencia_tecnica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cltech.assistencia_tecnica.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByEmail(String email);
}

