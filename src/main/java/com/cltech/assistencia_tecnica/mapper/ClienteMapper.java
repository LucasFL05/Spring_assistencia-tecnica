package com.cltech.assistencia_tecnica.mapper;

import com.cltech.assistencia_tecnica.dto.ClienteDTO;
import com.cltech.assistencia_tecnica.model.Cliente;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteDTO clienteToClienteDTO(Cliente cliente);

    Cliente clienteDTOToCliente(ClienteDTO clienteDTO);
}
