package com.cltech.assistencia_tecnica.controller;

import com.cltech.assistencia_tecnica.dto.OrdemDeServicoDTO;
import com.cltech.assistencia_tecnica.exception.GlobalExceptionHandler;
import com.cltech.assistencia_tecnica.exception.base.EntidadeNaoEncontradaException;
import com.cltech.assistencia_tecnica.service.OrdemDeServicoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrdemDeServicoControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private OrdemDeServicoService ordemDeServicoService;

    @InjectMocks
    private OrdemDeServicoController ordemDeServicoController;

    private OrdemDeServicoDTO exemploDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Corrige a serialização de datas
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc = MockMvcBuilders.standaloneSetup(ordemDeServicoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        exemploDto = new OrdemDeServicoDTO();
        exemploDto.setId(1L);
        exemploDto.setDescricaoProblema("Troca de tela");
        exemploDto.setStatus("ABERTA");
        exemploDto.setDataAbertura(LocalDateTime.now());
        exemploDto.setDispositivoId(42L);
    }

    @Test
    void criarOrdemDeServico_DeveRetornarOrdemCriadaComStatus200() throws Exception {
        when(ordemDeServicoService.criarOrdemDeServico(any(OrdemDeServicoDTO.class)))
                .thenReturn(exemploDto);

        mockMvc.perform(post("/api/ordens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exemploDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("ABERTA"));

        verify(ordemDeServicoService, times(1)).criarOrdemDeServico(any(OrdemDeServicoDTO.class));
    }

    @Test
    void criarOrdemDeServico_ComDadosInvalidos_DeveRetornarBadRequest() throws Exception {
        OrdemDeServicoDTO dtoInvalido = new OrdemDeServicoDTO(); // Campos obrigatórios faltando

        mockMvc.perform(post("/api/ordens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").exists());
    }

    @Test
    void buscarOrdemPorId_IdExistente_DeveRetornarOrdem() throws Exception {
        when(ordemDeServicoService.buscarOrdemPorId(1L)).thenReturn(exemploDto);

        mockMvc.perform(get("/api/ordens/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("ABERTA"));

        verify(ordemDeServicoService, times(1)).buscarOrdemPorId(1L);
    }

    @Test
    void buscarOrdemPorId_IdInexistente_DeveRetornarNotFound() throws Exception {
        when(ordemDeServicoService.buscarOrdemPorId(999L))
                .thenThrow(new EntidadeNaoEncontradaException("Ordem não encontrada"));

        mockMvc.perform(get("/api/ordens/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Ordem não encontrada"));
    }

    @Test
    void listarOrdens_DeveRetornarLista() throws Exception {
        when(ordemDeServicoService.listarOrdens()).thenReturn(List.of(exemploDto));

        mockMvc.perform(get("/api/ordens"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void atualizarStatus_EntradaValida_DeveRetornarOrdemAtualizada() throws Exception {
        OrdemDeServicoDTO atualizado = new OrdemDeServicoDTO();
        atualizado.setId(1L);
        atualizado.setStatus("CONCLUIDA");

        when(ordemDeServicoService.atualizarStatus(eq(1L), eq("CONCLUIDA")))
                .thenReturn(atualizado);

        mockMvc.perform(put("/api/ordens/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("status", "CONCLUIDA"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONCLUIDA"));

        verify(ordemDeServicoService, times(1)).atualizarStatus(1L, "CONCLUIDA");
    }

    @Test
    void deletarOrdem_EntradaValida_DeveRetornarNoContent() throws Exception {
        doNothing().when(ordemDeServicoService).deletarOrdem(1L);

        mockMvc.perform(delete("/api/ordens/1"))
                .andExpect(status().isNoContent());

        verify(ordemDeServicoService, times(1)).deletarOrdem(1L);
    }

    @Test
    void deletarOrdem_IdInexistente_DeveRetornarNotFound() throws Exception {
        doThrow(new EntidadeNaoEncontradaException("Ordem não encontrada"))
                .when(ordemDeServicoService).deletarOrdem(999L);

        mockMvc.perform(delete("/api/ordens/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Ordem não encontrada"));
    }

    @Test
    void erroGenerico_DeveRetornarInternalServerError() throws Exception {
        when(ordemDeServicoService.listarOrdens()).thenThrow(new RuntimeException("Erro genérico"));

        mockMvc.perform(get("/api/ordens"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.erro").value("Erro interno"));
    }
}