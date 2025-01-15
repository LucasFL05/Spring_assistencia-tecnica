package com.cltech.assistencia_tecnica.repository;

import com.cltech.assistencia_tecnica.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
