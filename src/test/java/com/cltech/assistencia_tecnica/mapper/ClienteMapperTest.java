package com.cltech.assistencia_tecnica.mapper;

import com.cltech.assistencia_tecnica.dto.ClienteDTO;
import com.cltech.assistencia_tecnica.model.Cliente;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class ClienteMapperTest {

  private final ClienteMapper mapper = Mappers.getMapper(ClienteMapper.class);

  @Test
  void shouldMapClienteToClienteDTO() {
    Cliente cliente = new Cliente(1L, "Lucas Ferreira", "lucas@email.com", "11999999999", null);

    ClienteDTO dto = mapper.clienteToClienteDTO(cliente);

    assertNotNull(dto);
    assertEquals(cliente.getId(), dto.getId());
    assertEquals(cliente.getNome(), dto.getNome());
    assertEquals(cliente.getEmail(), dto.getEmail());
    assertEquals(cliente.getTelefone(), dto.getTelefone());
  }

  @Test
  void shouldMapClienteDTOToCliente() {
    ClienteDTO dto = new ClienteDTO(1L, "Lucas Ferreira", "lucas@email.com", "11999999999");

    Cliente cliente = mapper.clienteDTOToCliente(dto);

    assertNotNull(cliente);
    assertEquals(dto.getId(), cliente.getId());
    assertEquals(dto.getNome(), cliente.getNome());
    assertEquals(dto.getEmail(), cliente.getEmail());
    assertEquals(dto.getTelefone(), cliente.getTelefone());
  }
}
