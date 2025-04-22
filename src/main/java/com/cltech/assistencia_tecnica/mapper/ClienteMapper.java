package com.cltech.assistencia_tecnica.mapper;

import com.cltech.assistencia_tecnica.dto.ClienteDTO;
import com.cltech.assistencia_tecnica.model.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteMapper INSTANCE = Mappers.getMapper(ClienteMapper.class);

    ClienteDTO clienteToClienteDTO(Cliente cliente);

    Cliente clienteDTOToCliente(ClienteDTO clienteDTO);
}
