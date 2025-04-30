package com.cltech.assistencia_tecnica.service.impl;

import com.cltech.assistencia_tecnica.exception.base.ConflitoDeDadosException;
import com.cltech.assistencia_tecnica.exception.base.EntidadeNaoEncontradaException;
import com.cltech.assistencia_tecnica.model.Cliente;
import com.cltech.assistencia_tecnica.repository.ClienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    // --- criarCliente ---

    @Test
    @DisplayName("createClient: should save when email is available")
    void criarCliente_savesWhenEmailAvailable() {
        Cliente toSave = new Cliente(null, "Ana", "ana@mail.com", "1111", null);
        Cliente saved = new Cliente(1L, "Ana", "ana@mail.com", "1111", null);

        when(clienteRepository.existsByEmail(toSave.getEmail())).thenReturn(false);
        when(clienteRepository.save(toSave)).thenReturn(saved);

        Cliente result = clienteService.criarCliente(toSave);

        assertThat(result.getId()).isEqualTo(1L);
        verify(clienteRepository).save(toSave);
    }

    @Test
    @DisplayName("createClient: should throw on duplicate email")
    void criarCliente_throwsWhenEmailDuplicated() {
        Cliente toSave = new Cliente(null, "Ana", "ana@mail.com", "1111", null);

        when(clienteRepository.existsByEmail(toSave.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> clienteService.criarCliente(toSave))
                .isInstanceOf(ConflitoDeDadosException.class)
                .hasMessageContaining("E-mail já está em uso");
        verify(clienteRepository, never()).save(any());
    }

    // --- buscarClientePorId ---

    @Test
    @DisplayName("findById: should return client when exists")
    void buscarClientePorId_returnsWhenFound() {
        Cliente cliente = new Cliente(1L, "Bob", "bob@mail.com", "2222", null);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente result = clienteService.buscarClientePorId(1L);

        assertThat(result).isEqualTo(cliente);
    }

    @Test
    @DisplayName("findById: should throw when not found")
    void buscarClientePorId_throwsWhenNotFound() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.buscarClientePorId(42L))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("Cliente não encontrado");
    }

    // --- listarClientes ---

    @Test
    @DisplayName("listAll: should return all clients")
    void listarClientes_returnsAll() {
        List<Cliente> lista = List.of(
                new Cliente(1L, "C1", "c1@mail.com", "3333", null),
                new Cliente(2L, "C2", "c2@mail.com", "4444", null)
        );
        when(clienteRepository.findAll()).thenReturn(lista);

        List<Cliente> result = clienteService.listarClientes();

        assertThat(result).hasSize(2).containsAll(lista);
    }

    @Test
    @DisplayName("listAll: should return empty list when none")
    void listarClientes_returnsEmpty() {
        when(clienteRepository.findAll()).thenReturn(Collections.emptyList());

        List<Cliente> result = clienteService.listarClientes();

        assertThat(result).isEmpty();
    }

    // --- atualizarCliente ---

    @Test
    @DisplayName("updateClient: should update when data valid and same email")
    void atualizarCliente_updatesWhenSameEmail() {
        Cliente existing = new Cliente(1L, "Dave", "dave@mail.com", "5555", null);
        Cliente update = new Cliente(null, "Dave New", "dave@mail.com", "6666", null);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(clienteRepository.existsByEmail(update.getEmail())).thenReturn(true);
        when(clienteRepository.save(existing)).thenAnswer(invocation -> existing);

        Cliente result = clienteService.atualizarCliente(1L, update);

        assertThat(result.getNome()).isEqualTo("Dave New");
        assertThat(result.getTelefone()).isEqualTo("6666");
        verify(clienteRepository).save(existing);
    }

    @Test
    @DisplayName("updateClient: should update when data valid and new email available")
    void atualizarCliente_updatesWhenNewEmailAvailable() {
        Cliente existing = new Cliente(1L, "Eve", "eve@mail.com", "7777", null);
        Cliente update = new Cliente(null, "Eve New", "new@mail.com", "8888", null);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(clienteRepository.existsByEmail(update.getEmail())).thenReturn(false);
        when(clienteRepository.save(existing)).thenAnswer(invocation -> existing);

        Cliente result = clienteService.atualizarCliente(1L, update);

        assertThat(result.getNome()).isEqualTo("Eve New");
        assertThat(result.getEmail()).isEqualTo("new@mail.com");
        verify(clienteRepository).save(existing);
    }

    @Test
    @DisplayName("updateClient: should throw on email conflict with another client")
    void atualizarCliente_throwsWhenEmailConflict() {
        Cliente existing = new Cliente(1L, "Frank", "frank@mail.com", "9999", null);
        Cliente update = new Cliente(null, "Frank", "conflict@mail.com", "9999", null);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(clienteRepository.existsByEmail(update.getEmail())).thenReturn(true);
        assertThatThrownBy(() -> clienteService.atualizarCliente(1L, update))
                .isInstanceOf(ConflitoDeDadosException.class)
                .hasMessageContaining("E-mail já está em uso");
        verify(clienteRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateClient: should throw when client not found")
    void atualizarCliente_throwsWhenNotFound() {
        Cliente update = new Cliente(null, "Novo", "novo@mail.com", "1234", null);
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.atualizarCliente(99L, update))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("Cliente não encontrado");
    }


    // --- deletarCliente ---

    @Test
    @DisplayName("deleteClient: should delete when no integrity violation")
    void deletarCliente_deletesWhenNoViolation() {
        Cliente cliente = new Cliente(1L, "Gina", "gina@mail.com", "0000", null);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        clienteService.deletarCliente(1L);

        verify(clienteRepository).delete(cliente);
    }

    @Test
    @DisplayName("deleteClient: should throw on integrity violation")
    void deletarCliente_throwsOnIntegrityViolation() {
        Cliente cliente = new Cliente(1L, "Harry", "harry@mail.com", "1111", null);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        doThrow(new DataIntegrityViolationException("fk_violation"))
                .when(clienteRepository).delete(cliente);

        assertThatThrownBy(() -> clienteService.deletarCliente(1L))
                .isInstanceOf(ConflitoDeDadosException.class)
                .hasMessageContaining("Não é possível excluir");
    }

    @Test
    @DisplayName("deleteClient: should throw when client not found")
    void deletarCliente_throwsWhenNotFound() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.deletarCliente(99L))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("Cliente não encontrado");
    }
}
