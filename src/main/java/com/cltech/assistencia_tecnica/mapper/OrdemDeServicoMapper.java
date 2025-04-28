package com.cltech.assistencia_tecnica.mapper;

import com.cltech.assistencia_tecnica.dto.OrdemDeServicoDTO;
import com.cltech.assistencia_tecnica.model.OrdemDeServico;
import com.cltech.assistencia_tecnica.model.StatusOrdemServico;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OrdemDeServicoMapper {

    @Mapping(source = "dispositivo.id", target = "dispositivoId")
    @Mapping(source = "status", target = "status", qualifiedByName = "statusToString")
    OrdemDeServicoDTO toDto(OrdemDeServico entity);

    @Mapping(source = "dispositivoId", target = "dispositivo.id")
    @Mapping(source = "status", target = "status", qualifiedByName = "stringToStatus")
    OrdemDeServico toEntity(OrdemDeServicoDTO dto);

    @Named("statusToString")
    static String mapStatusToString(StatusOrdemServico status) {
        return status.name();
    }

    @Named("stringToStatus")
    static StatusOrdemServico mapStringToStatus(String status) {
        try {
            return StatusOrdemServico.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Status inválido: " + status);
        }
    }
}
