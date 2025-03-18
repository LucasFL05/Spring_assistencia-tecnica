package com.cltech.assistencia_tecnica.service.impl;

import com.cltech.assistencia_tecnica.model.Cliente;
import com.cltech.assistencia_tecnica.repository.ClienteRepository;
import com.cltech.assistencia_tecnica.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Cliente criarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente buscarClientePorId(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    @Override
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente atualizarCliente(Long id, Cliente clienteAtualizado) {
        Cliente cliente = buscarClientePorId(id);
        cliente.setNome(clienteAtualizado.getNome());
        cliente.setEmail(clienteAtualizado.getEmail());
        cliente.setTelefone(clienteAtualizado.getTelefone());
        return clienteRepository.save(cliente);
    }

    @Override
    public void deletarCliente(Long id) {
        clienteRepository.deleteById(id);
    }
}