package com.cltech.assistencia_tecnica.mapper;

import com.cltech.assistencia_tecnica.dto.DispositivoDTO;
import com.cltech.assistencia_tecnica.model.Cliente;
import com.cltech.assistencia_tecnica.model.Dispositivo;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class DispositivoMapperTest {

    private final DispositivoMapper mapper = Mappers.getMapper(DispositivoMapper.class);

    @Test
    void shouldMapDispositivoToDispositivoDTO() {
        // Arrange
        Cliente cliente = new Cliente(1L, "João Silva", "joao@mail.com", "+55 11 99999-0000");
        Dispositivo dispositivo = new Dispositivo(1L, "Smartphone", "Samsung", "Galaxy S21", cliente, null);

        // Act
        DispositivoDTO dto = mapper.dispositivoToDispositivoDTO(dispositivo);

        // Assert
        assertNotNull(dto);
        assertEquals(dispositivo.getId(), dto.getId());
        assertEquals(dispositivo.getTipo(), dto.getTipo());
        assertEquals(dispositivo.getMarca(), dto.getMarca());
        assertEquals(dispositivo.getModelo(), dto.getModelo());
        assertEquals(dispositivo.getCliente().getId(), dto.getClienteId());
    }

    @Test
    void shouldMapDispositivoDTOToDispositivo() {
        // Arrange
        DispositivoDTO dto = new DispositivoDTO(1L, "Smartphone", "Samsung", "Galaxy S21", null);

        // Act
        Dispositivo dispositivo = mapper.dispositivoDTOToDispositivo(dto);

        // Assert
        assertNotNull(dispositivo);
        assertEquals(dto.getId(), dispositivo.getId());
        assertEquals(dto.getTipo(), dispositivo.getTipo());
        assertEquals(dto.getMarca(), dispositivo.getMarca());
        assertEquals(dto.getModelo(), dispositivo.getModelo());
        assertEquals(dto.getClienteId(), dispositivo.getCliente().getId());
    }
}