package com.cltech.assistencia_tecnica.service.impl;

import com.cltech.assistencia_tecnica.exception.base.ConflitoDeDadosException;
import com.cltech.assistencia_tecnica.exception.base.EntidadeNaoEncontradaException;
import com.cltech.assistencia_tecnica.model.Cliente;
import com.cltech.assistencia_tecnica.repository.ClienteRepository;
import com.cltech.assistencia_tecnica.service.ClienteService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente criarCliente(Cliente cliente) {
        validarEmailDisponivel(cliente.getEmail(), null);
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente buscarClientePorId(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado com o ID: " + id));
    }

    @Override
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente atualizarCliente(Long id, Cliente clienteAtualizado) {
        Cliente clienteExistente = buscarClientePorId(id);
        validarEmailDisponivel(clienteAtualizado.getEmail(), id);

        clienteExistente.setNome(clienteAtualizado.getNome());
        clienteExistente.setEmail(clienteAtualizado.getEmail());
        clienteExistente.setTelefone(clienteAtualizado.getTelefone());

        return clienteRepository.save(clienteExistente);
    }

    @Override
    public void deletarCliente(Long id) {
        Cliente cliente = buscarClientePorId(id);
        try {
            clienteRepository.delete(cliente);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflitoDeDadosException("Não é possível excluir o cliente com ID " + id + " pois ele está relacionado a outros registros.");
        }
    }

    private void validarEmailDisponivel(String email, Long idAtual) {
        boolean emailEmUso = clienteRepository.existsByEmail(email);
        if (emailEmUso && (idAtual == null || !clienteRepository.findById(idAtual).map(c -> c.getEmail().equals(email)).orElse(false))) {
            throw new ConflitoDeDadosException("E-mail já está em uso por outro cliente.");
        }
    }

    @Override
    public Cliente buscarEntidadePorId(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado com o ID: " + id));
    }
}
