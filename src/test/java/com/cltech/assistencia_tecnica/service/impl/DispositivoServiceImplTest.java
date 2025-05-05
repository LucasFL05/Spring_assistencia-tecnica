package com.cltech.assistencia_tecnica.service.impl;

import com.cltech.assistencia_tecnica.dto.ClienteDTO;
import com.cltech.assistencia_tecnica.dto.DispositivoDTO;
import com.cltech.assistencia_tecnica.exception.base.ConflitoDeDadosException;
import com.cltech.assistencia_tecnica.exception.base.EntidadeNaoEncontradaException;
import com.cltech.assistencia_tecnica.mapper.DispositivoMapper;
import com.cltech.assistencia_tecnica.model.Cliente;
import com.cltech.assistencia_tecnica.model.Dispositivo;
import com.cltech.assistencia_tecnica.model.OrdemDeServico;
import com.cltech.assistencia_tecnica.repository.DispositivoRepository;
import com.cltech.assistencia_tecnica.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DispositivoServiceImplTest {

    @Mock
    private DispositivoRepository dispositivoRepository;

    @Mock
    private ClienteService clienteService;

    @Mock
    private DispositivoMapper dispositivoMapper;

    @InjectMocks
    private DispositivoServiceImpl service;

    private static final Long DEVICE_ID  = 1L;
    private static final Long CLIENT_ID  = 10L;

    private ClienteDTO    clienteDTO;
    private DispositivoDTO dto;
    private Cliente       cliente;
    private Dispositivo   entity;

    @BeforeEach
    void setup() {
        // DTO and nested ClienteDTO
        clienteDTO = new ClienteDTO(CLIENT_ID, "João Silva", "joao@mail.com", "+55 11 99999-0000");
        dto        = new DispositivoDTO(DEVICE_ID, "Notebook", "Dell", "XPS 13", clienteDTO);

        // Entity and linked Cliente
        cliente = new Cliente(CLIENT_ID, "João Silva", "joao@mail.com", "+55 11 99999-0000", null);
        entity  = new Dispositivo(DEVICE_ID, "Notebook", "Dell", "XPS 13", cliente, List.of());
    }

    @Test
    @DisplayName("create: should map DTO → entity, save and return DTO")
    void shouldCreateAndMap() {
        // Arrange
        when(dispositivoMapper.dispositivoDTOToDispositivo(dto)).thenReturn(entity);
        when(clienteService.buscarEntidadePorId(CLIENT_ID)).thenReturn(cliente);
        when(dispositivoRepository.save(entity)).thenReturn(entity);
        when(dispositivoMapper.dispositivoToDispositivoDTO(entity)).thenReturn(dto);

        // Act
        DispositivoDTO result = service.criarDispositivo(dto);

        // Assert
        assertThat(result).isEqualTo(dto);
        verify(dispositivoMapper).dispositivoDTOToDispositivo(dto);
        verify(clienteService).buscarEntidadePorId(CLIENT_ID);
        verify(dispositivoRepository).save(entity);
        verify(dispositivoMapper).dispositivoToDispositivoDTO(entity);
    }

    @Test
    @DisplayName("getById: should return DTO when found")
    void shouldReturnDTOWhenFound() {
        // Arrange
        when(dispositivoRepository.findById(DEVICE_ID)).thenReturn(Optional.of(entity));
        when(dispositivoMapper.dispositivoToDispositivoDTO(entity)).thenReturn(dto);

        // Act
        DispositivoDTO result = service.buscarDispositivoPorId(DEVICE_ID);

        // Assert
        assertThat(result).isEqualTo(dto);
        verify(dispositivoRepository).findById(DEVICE_ID);
    }

    @Test
    @DisplayName("getById: should throw when not found")
    void shouldThrowWhenNotFound() {
        // Arrange
        when(dispositivoRepository.findById(DEVICE_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.buscarDispositivoPorId(DEVICE_ID))
                .isInstanceOf(EntidadeNaoEncontradaException.class)
                .hasMessageContaining("Dispositivo não encontrado com o ID");
    }

    @Test
    @DisplayName("listAll: should return list of DTOs")
    void shouldListAllDTOs() {
        // Arrange
        when(dispositivoRepository.findAll()).thenReturn(List.of(entity));
        when(dispositivoMapper.dispositivoToDispositivoDTO(entity)).thenReturn(dto);

        // Act
        List<DispositivoDTO> result = service.listarDispositivos();

        // Assert
        assertThat(result).hasSize(1).containsExactly(dto);
        verify(dispositivoRepository).findAll();
    }

    @Test
    @DisplayName("update: should update entity and return DTO")
    void shouldUpdateAndMap() {
        // Arrange
        when(dispositivoRepository.findById(DEVICE_ID)).thenReturn(Optional.of(entity));
        when(dispositivoMapper.dispositivoDTOToDispositivo(dto)).thenReturn(entity);
        when(clienteService.buscarEntidadePorId(CLIENT_ID)).thenReturn(cliente);
        when(dispositivoRepository.save(entity)).thenReturn(entity);
        when(dispositivoMapper.dispositivoToDispositivoDTO(entity)).thenReturn(dto);

        // Act
        DispositivoDTO result = service.atualizarDispositivo(DEVICE_ID, dto);

        // Assert
        assertThat(result).isEqualTo(dto);
        verify(dispositivoRepository).findById(DEVICE_ID);
        verify(dispositivoRepository).save(entity);
    }

    @Test
    @DisplayName("delete: should delete when no orders exist")
    void shouldDeleteWhenNoOrders() {
        // Arrange
        when(dispositivoRepository.findById(DEVICE_ID)).thenReturn(Optional.of(entity));

        // Act
        service.deletarDispositivo(DEVICE_ID);

        // Assert
        verify(dispositivoRepository).delete(entity);
    }

    @Test
    @DisplayName("delete: should throw when orders exist")
    void shouldThrowWhenOrdersExist() {
        // Arrange
        entity.setOrdensDeServico(List.of(new OrdemDeServico()));
        when(dispositivoRepository.findById(DEVICE_ID)).thenReturn(Optional.of(entity));

        // Act & Assert
        assertThatThrownBy(() -> service.deletarDispositivo(DEVICE_ID))
                .isInstanceOf(ConflitoDeDadosException.class)
                .hasMessageContaining("pois existem ordens de serviço associadas");

        verify(dispositivoRepository, never()).delete(entity);
    }

    @Test
    @DisplayName("delete: should wrap DataIntegrityViolationException")
    void shouldWrapDataIntegrityViolation() {
        // Arrange
        when(dispositivoRepository.findById(DEVICE_ID)).thenReturn(Optional.of(entity));
        doThrow(new DataIntegrityViolationException("fk_violation"))
                .when(dispositivoRepository).delete(entity);

        // Act & Assert
        assertThatThrownBy(() -> service.deletarDispositivo(DEVICE_ID))
                .isInstanceOf(ConflitoDeDadosException.class)
                .hasMessageContaining("violação de integridade de dados");

        verify(dispositivoRepository).delete(entity);
    }
}
