package com.cltech.assistencia_tecnica.mapper;

import com.cltech.assistencia_tecnica.dto.OrdemDeServicoDTO;
import com.cltech.assistencia_tecnica.model.Dispositivo;
import com.cltech.assistencia_tecnica.model.OrdemDeServico;
import com.cltech.assistencia_tecnica.model.StatusOrdemServico;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrdemDeServicoMapperTest {

    private final OrdemDeServicoMapper mapper = Mappers.getMapper(OrdemDeServicoMapper.class);

    @Test
    void shouldMapOrdemDeServicoToOrdemDeServicoDTO() {
        // Arrange
        Dispositivo dispositivo = new Dispositivo();
        dispositivo.setId(1L);

        OrdemDeServico ordem = new OrdemDeServico();
        ordem.setId(1L);
        ordem.setDescricaoProblema("Tela quebrada");
        ordem.setStatus(StatusOrdemServico.ABERTA);
        ordem.setDataAbertura(LocalDateTime.now());
        ordem.setDataConclusao(null);
        ordem.setDispositivo(dispositivo);

        // Act
        OrdemDeServicoDTO dto = mapper.toDto(ordem);

        // Assert
        assertNotNull(dto);
        assertEquals(ordem.getId(), dto.getId());
        assertEquals(ordem.getDescricaoProblema(), dto.getDescricaoProblema());
        assertEquals(ordem.getStatus().name(), dto.getStatus());
        assertEquals(ordem.getDataAbertura(), dto.getDataAbertura());
        assertEquals(ordem.getDataConclusao(), dto.getDataConclusao());
        assertEquals(ordem.getDispositivo().getId(), dto.getDispositivoId());
    }

    @Test
    void shouldMapOrdemDeServicoDTOToOrdemDeServico() {
        // Arrange
        OrdemDeServicoDTO dto = new OrdemDeServicoDTO();
        dto.setId(1L);
        dto.setDescricaoProblema("Tela quebrada");
        dto.setStatus("ABERTA");
        dto.setDataAbertura(LocalDateTime.now());
        dto.setDataConclusao(null);
        dto.setDispositivoId(1L);

        // Act
        OrdemDeServico ordem = mapper.toEntity(dto);

        // Assert
        assertNotNull(ordem);
        assertEquals(dto.getId(), ordem.getId());
        assertEquals(dto.getDescricaoProblema(), ordem.getDescricaoProblema());
        assertEquals(StatusOrdemServico.valueOf(dto.getStatus()), ordem.getStatus());
        assertEquals(dto.getDataAbertura(), ordem.getDataAbertura());
        assertEquals(dto.getDataConclusao(), ordem.getDataConclusao());
        assertEquals(dto.getDispositivoId(), ordem.getDispositivo().getId());
    }
}