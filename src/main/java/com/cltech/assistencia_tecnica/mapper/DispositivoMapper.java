package com.cltech.assistencia_tecnica.mapper;

import com.cltech.assistencia_tecnica.dto.DispositivoDTO;
import com.cltech.assistencia_tecnica.model.Dispositivo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DispositivoMapper {

    @Mapping(source = "cliente.id", target = "clienteId")
    DispositivoDTO dispositivoToDispositivoDTO(Dispositivo dispositivo);

    @Mapping(source = "clienteId",  target = "cliente.id")
    Dispositivo dispositivoDTOToDispositivo(DispositivoDTO dispositivoDTO);
}
