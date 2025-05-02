package com.cltech.assistencia_tecnica.service;

import com.cltech.assistencia_tecnica.model.Cliente;

import java.util.List;

public interface ClienteService {
    Cliente criarCliente(Cliente cliente);
    Cliente buscarClientePorId(Long id);
    List<Cliente> listarClientes();
    Cliente atualizarCliente(Long id, Cliente clienteAtualizado);
    void deletarCliente(Long id);

    Cliente buscarEntidadePorId(Long id);
}