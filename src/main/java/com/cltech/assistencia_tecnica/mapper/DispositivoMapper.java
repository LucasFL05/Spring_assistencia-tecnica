package com.cltech.assistencia_tecnica.mapper;

import com.cltech.assistencia_tecnica.dto.DispositivoDTO;
import com.cltech.assistencia_tecnica.model.Dispositivo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DispositivoMapper {

    DispositivoDTO dispositivoToDispositivoDTO(Dispositivo dispositivo);

    Dispositivo dispositivoDTOToDispositivo(DispositivoDTO dispositivoDTO);
}
