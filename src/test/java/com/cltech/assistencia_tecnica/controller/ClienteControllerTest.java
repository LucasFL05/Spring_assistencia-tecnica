package com.cltech.assistencia_tecnica.controller;

import com.cltech.assistencia_tecnica.dto.ClienteDTO;
import com.cltech.assistencia_tecnica.mapper.ClienteMapper;
import com.cltech.assistencia_tecnica.model.Cliente;
import com.cltech.assistencia_tecnica.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ClienteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClienteService clienteService;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteController clienteController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void deveCriarCliente() throws Exception {
        // Arrange
        ClienteDTO dtoEntrada = new ClienteDTO(null, "Lucas", "lucas@email.com", "11999999999");
        Cliente clienteEntidade = new Cliente(1L, "Lucas", "lucas@email.com", "11999999999");
        ClienteDTO dtoSaida = new ClienteDTO(1L, "Lucas", "lucas@email.com", "11999999999");

        when(clienteMapper.clienteDTOToCliente(dtoEntrada)).thenReturn(clienteEntidade);
        when(clienteService.criarCliente(clienteEntidade)).thenReturn(clienteEntidade);
        when(clienteMapper.clienteToClienteDTO(clienteEntidade)).thenReturn(dtoSaida);

        // Act + Assert
        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoEntrada)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/clientes/1"))
                .andExpect(content().json(objectMapper.writeValueAsString(dtoSaida)));
    }

    @Test
    void deveBuscarClientePorId() throws Exception {
        Cliente cliente = new Cliente(1L, "Lucas", "lucas@email.com", "11999999999");
        ClienteDTO dto = new ClienteDTO(1L, "Lucas", "lucas@email.com", "11999999999");

        when(clienteService.buscarClientePorId(1L)).thenReturn(cliente);
        when(clienteMapper.clienteToClienteDTO(cliente)).thenReturn(dto);

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dto)));
    }

    @Test
    void deveListarClientes() throws Exception {
        Cliente cliente = new Cliente(1L, "Lucas", "lucas@email.com", "11999999999");
        ClienteDTO dto = new ClienteDTO(1L, "Lucas", "lucas@email.com", "11999999999");

        when(clienteService.listarClientes()).thenReturn(List.of(cliente));
        when(clienteMapper.clienteToClienteDTO(cliente)).thenReturn(dto);

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(dto))));
    }

    @Test
    void deveAtualizarCliente() throws Exception {
        ClienteDTO dtoEntrada = new ClienteDTO(null, "Lucas Atualizado", "novo@email.com", "11900000000");
        Cliente clienteEntrada = new Cliente(null, "Lucas Atualizado", "novo@email.com", "11900000000");
        Cliente clienteAtualizado = new Cliente(1L, "Lucas Atualizado", "novo@email.com", "11900000000");
        ClienteDTO dtoSaida = new ClienteDTO(1L, "Lucas Atualizado", "novo@email.com", "11900000000");

        when(clienteMapper.clienteDTOToCliente(dtoEntrada)).thenReturn(clienteEntrada);
        when(clienteService.atualizarCliente(1L, clienteEntrada)).thenReturn(clienteAtualizado);
        when(clienteMapper.clienteToClienteDTO(clienteAtualizado)).thenReturn(dtoSaida);

        mockMvc.perform(put("/api/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoEntrada)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dtoSaida)));
    }

    @Test
    void deveDeletarCliente() throws Exception {
        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isNoContent());
    }
}
