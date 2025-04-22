package com.cltech.assistencia_tecnica.mapper;

import com.cltech.assistencia_tecnica.dto.OrdemDeServicoDTO;
import com.cltech.assistencia_tecnica.model.OrdemDeServico;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrdemDeServicoMapper {

    OrdemDeServicoMapper INSTANCE = Mappers.getMapper(OrdemDeServicoMapper.class);

    @Mapping(source = "dispositivo.id", target = "dispositivoId")
    @Mapping(source = "status", target = "status", qualifiedByName = "statusToString")
    OrdemDeServicoDTO toDto(OrdemDeServico entity);

    @Mapping(source = "dispositivoId", target = "dispositivo.id")
    @Mapping(source = "status", target = "status", qualifiedByName = "stringToStatus")
    OrdemDeServico toEntity(OrdemDeServicoDTO dto);

    @Named("statusToString")
    static String mapStatusToString(Enum<?> status) {
        return status.name();
    }

    @Named("stringToStatus")
    static Enum<?> mapStringToStatus(String status) {
        try {
            return Enum.valueOf(com.cltech.assistencia_tecnica.model.StatusOrdemServico.class, status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Status inválido: " + status);
        }
    }
}
