package com.cltech.assistencia_tecnica.controller;

import com.cltech.assistencia_tecnica.dto.ClienteDTO;
import com.cltech.assistencia_tecnica.dto.DispositivoDTO;
import com.cltech.assistencia_tecnica.exception.GlobalExceptionHandler;
import com.cltech.assistencia_tecnica.exception.base.ConflitoDeDadosException;
import com.cltech.assistencia_tecnica.exception.base.EntidadeNaoEncontradaException;
import com.cltech.assistencia_tecnica.service.DispositivoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DispositivoControllerTest {

    private MockMvc mockMvc;
    final private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private DispositivoService dispositivoService;

    @InjectMocks
    private DispositivoController dispositivoController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dispositivoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createDevice_ValidInput_ShouldReturnCreated() throws Exception {
        ClienteDTO cliente = new ClienteDTO(1L, "Lucas", "lucas@email.com", "123456789");

        DispositivoDTO input = new DispositivoDTO(null, "Notebook", "Dell", "XPS 15", 1L);
        DispositivoDTO created = new DispositivoDTO(1L, "Notebook", "Dell", "XPS 15", 1L);

        when(dispositivoService.criarDispositivo(any())).thenReturn(created);

        mockMvc.perform(post("/api/dispositivos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.tipo").value("Notebook"));
    }

    @Test
    void createDevice_InvalidInput_ShouldReturnBadRequest() throws Exception {
        DispositivoDTO invalidInput = new DispositivoDTO(null, "", "", "", null);

        mockMvc.perform(post("/api/dispositivos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidInput)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalhes").isArray());
    }

    @Test
    void getDeviceById_ExistingId_ShouldReturnDevice() throws Exception {
        ClienteDTO cliente = new ClienteDTO(1L, "Lucas", "lucas@email.com", "123456789");
        DispositivoDTO device = new DispositivoDTO(1L, "Smartphone", "Samsung", "Galaxy S21", 1L);
        when(dispositivoService.buscarDispositivoPorId(1L)).thenReturn(device);

        mockMvc.perform(get("/api/dispositivos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.tipo").value("Smartphone"));
    }

    @Test
    void getDeviceById_NonExistingId_ShouldReturnNotFound() throws Exception {
        when(dispositivoService.buscarDispositivoPorId(999L))
                .thenThrow(new EntidadeNaoEncontradaException("Dispositivo não encontrado"));

        mockMvc.perform(get("/api/dispositivos/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensagem").value("Dispositivo não encontrado"))
                .andExpect(jsonPath("$.erro").exists());
    }

    @Test
    void getAllDevices_ShouldReturnDeviceList() throws Exception {
        ClienteDTO cliente = new ClienteDTO(1L, "Lucas", "lucas@email.com", "123456789");
        DispositivoDTO device = new DispositivoDTO(1L, "Tablet", "Apple", "iPad Pro", 1L);
        when(dispositivoService.listarDispositivos()).thenReturn(List.of(device));

        mockMvc.perform(get("/api/dispositivos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].tipo").value("Tablet"));
    }

    @Test
    void updateDevice_ValidInput_ShouldReturnUpdatedDevice() throws Exception {
        ClienteDTO cliente = new ClienteDTO(1L, "Lucas", "lucas@email.com", "123456789");
        DispositivoDTO updated = new DispositivoDTO(1L, "Notebook", "Lenovo", "ThinkPad X1", 1L);
        when(dispositivoService.atualizarDispositivo(eq(1L), any())).thenReturn(updated);

        mockMvc.perform(put("/api/dispositivos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.marca").value("Lenovo"));
    }

    @Test
    void updateDevice_IdMismatch_ShouldReturnBadRequest() throws Exception {
        ClienteDTO cliente = new ClienteDTO(1L, "Lucas", "lucas@email.com", "123456789");
        DispositivoDTO input = new DispositivoDTO(2L, "Phone", "Apple", "iPhone 15", 1L);

        mockMvc.perform(put("/api/dispositivos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteDevice_NoAssociations_ShouldReturnNoContent() throws Exception {
        doNothing().when(dispositivoService).deletarDispositivo(1L);

        mockMvc.perform(delete("/api/dispositivos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteDevice_WithAssociations_ShouldReturnConflict() throws Exception {
        doThrow(new ConflitoDeDadosException("Existem ordens associadas"))
                .when(dispositivoService).deletarDispositivo(1L);

        mockMvc.perform(delete("/api/dispositivos/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void handleDataIntegrityViolation_ShouldReturnConflict() throws Exception {
        when(dispositivoService.criarDispositivo(any()))
                .thenThrow(new DataIntegrityViolationException("Violação de chave única"));

        ClienteDTO cliente = new ClienteDTO(1L, "Lucas", "lucas@email.com", "123456789");
        DispositivoDTO input = new DispositivoDTO(null, "Phone", "Apple", "iPhone 15", 1L);

        mockMvc.perform(post("/api/dispositivos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensagem").value("Violação de integridade de dados"));
    }

    @Test
    void handleGenericException_ShouldReturnInternalServerError() throws Exception {
        when(dispositivoService.listarDispositivos())
                .thenThrow(new RuntimeException("Erro inesperado"));

        mockMvc.perform(get("/api/dispositivos"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.erro").value("Erro interno"));
    }
}