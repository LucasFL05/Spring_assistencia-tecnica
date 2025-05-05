package com.cltech.assistencia_tecnica.service.impl;

import com.cltech.assistencia_tecnica.exception.base.ConflitoDeDadosException;
import com.cltech.assistencia_tecnica.exception.base.EntidadeNaoEncontradaException;
import com.cltech.assistencia_tecnica.model.Cliente;
import com.cltech.assistencia_tecnica.repository.ClienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteServiceImpl service;

    @Test
    @DisplayName("create: should save when email is available")
    void shouldSaveWhenEmailAvailable() {
        // Arrange
        Cliente toSave = new Cliente(null, "Ana", "ana@mail.com", "1111", null);
        Cliente saved  = new Cliente(1L,    "Ana", "ana@mail.com", "1111", null);

        when(repository.existsByEmail(toSave.getEmail())).thenReturn(false);
        when(repository.save(toSave)).thenReturn(saved);

        // Act
        Cliente result = service.criarCliente(toSave);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        verify(repository).save(toSave);
    }

    @Test
    @DisplayName("create: should throw on duplicate email")
    void shouldThrowOnDuplicateEmail() {
        // Arrange
        Cliente toSave = new Cliente(null, "Ana", "ana@mail.com", "1111", null);
        when(repository.existsByEmail(toSave.getEmail())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> service.criarCliente(toSave))
                .isInstanceOf(ConflitoDeDadosException.class)
                .hasMessageContaining("E-mail já está em uso");

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("getById: should return client when exists")
    void shouldReturnWhenFound() {
        // Arrange
        Cliente cliente = new Cliente(1L, "Bob", "bob@mail.com", "2222", null);
        when(repository.findById(1L)).thenReturn(Optional.of(cliente));

        // Act
        Cliente result = service.buscarClientePorId(1L);

        // Assert
        assertThat(result).isEqualTo(cliente);
    }

    @Test
    @DisplayName("getById: should throw when not found")
    void shouldThrowWhenNotFound() {
        // Arrange
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.buscarClientePorId(42L))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("Cliente não encontrado");
    }

    @Test
    @DisplayName("listAll: should return all clients")
    void shouldReturnAll() {
        // Arrange
        List<Cliente> lista = List.of(
                new Cliente(1L, "C1", "c1@mail.com", "3333", null),
                new Cliente(2L, "C2", "c2@mail.com", "4444", null)
        );
        when(repository.findAll()).thenReturn(lista);

        // Act
        List<Cliente> result = service.listarClientes();

        // Assert
        assertThat(result).hasSize(2).containsAll(lista);
    }

    @Test
    @DisplayName("listAll: should return empty list when none")
    void shouldReturnEmptyList() {
        // Arrange
        when(repository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Cliente> result = service.listarClientes();

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("update: should update when same email")
    void shouldUpdateWhenSameEmail() {
        // Arrange
        Cliente existing = new Cliente(1L, "Dave", "dave@mail.com", "5555", null);
        Cliente update   = new Cliente(null, "Dave New", "dave@mail.com", "6666", null);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.existsByEmail(update.getEmail())).thenReturn(true);
        when(repository.save(existing)).thenReturn(existing);

        // Act
        Cliente result = service.atualizarCliente(1L, update);

        // Assert
        assertThat(result.getNome()).isEqualTo("Dave New");
        assertThat(result.getTelefone()).isEqualTo("6666");
        verify(repository).save(existing);
    }

    @Test
    @DisplayName("update: should throw on email conflict")
    void shouldThrowOnEmailConflict() {
        // Arrange
        Cliente existing = new Cliente(1L, "Frank", "frank@mail.com", "9999", null);
        Cliente update   = new Cliente(null, "Frank", "conflict@mail.com", "9999", null);

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.existsByEmail(update.getEmail())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> service.atualizarCliente(1L, update))
                .isInstanceOf(ConflitoDeDadosException.class)
                .hasMessageContaining("E-mail já está em uso");

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("update: should throw when not found")
    void shouldThrowOnUpdateNotFound() {
        // Arrange
        Cliente update = new Cliente(null, "Novo", "novo@mail.com", "1234", null);
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.atualizarCliente(99L, update))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("Cliente não encontrado");
    }

    @Test
    @DisplayName("delete: should delete when no integrity violation")
    void shouldDeleteWhenNoViolation() {
        // Arrange
        Cliente cliente = new Cliente(1L, "Gina", "gina@mail.com", "0000", null);
        when(repository.findById(1L)).thenReturn(Optional.of(cliente));

        // Act
        service.deletarCliente(1L);

        // Assert
        verify(repository).delete(cliente);
    }

    @Test
    @DisplayName("delete: should throw on integrity violation")
    void shouldThrowOnDeleteIntegrityViolation() {
        // Arrange
        Cliente cliente = new Cliente(1L, "Harry", "harry@mail.com", "1111", null);
        when(repository.findById(1L)).thenReturn(Optional.of(cliente));
        doThrow(new DataIntegrityViolationException("fk_violation"))
                .when(repository).delete(cliente);

        // Act & Assert
        assertThatThrownBy(() -> service.deletarCliente(1L))
                .isInstanceOf(ConflitoDeDadosException.class)
                .hasMessageContaining("Não é possível excluir");
    }

    @Test
    @DisplayName("delete: should throw when not found")
    void shouldThrowOnDeleteNotFound() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.deletarCliente(99L))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("Cliente não encontrado");
    }
}
