package com.cltech.assistencia_tecnica.mapper;

import com.cltech.assistencia_tecnica.dto.DispositivoDTO;
import com.cltech.assistencia_tecnica.model.Dispositivo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DispositivoMapper {

    DispositivoMapper INSTANCE = Mappers.getMapper(DispositivoMapper.class);

    DispositivoDTO dispositivoToDispositivoDTO(Dispositivo dispositivo);

    Dispositivo dispositivoDTOToDispositivo(DispositivoDTO dispositivoDTO);
}
