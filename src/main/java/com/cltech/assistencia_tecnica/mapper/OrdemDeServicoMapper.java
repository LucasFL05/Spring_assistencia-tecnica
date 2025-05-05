package com.cltech.assistencia_tecnica.mapper;

import com.cltech.assistencia_tecnica.dto.OrdemDeServicoDTO;
import com.cltech.assistencia_tecnica.model.OrdemDeServico;
import com.cltech.assistencia_tecnica.model.StatusOrdemServico;
import org.mapstruct.*;

import java.util.Arrays;

@Mapper(componentModel = "spring")
public interface OrdemDeServicoMapper {

    @Mapping(source = "dispositivo.id", target = "dispositivoId")
    @Mapping(source = "status", target = "status", qualifiedByName = "statusToString")
    OrdemDeServicoDTO toDto(OrdemDeServico entity);

    @Mapping(source = "dispositivoId", target = "dispositivo.id")
    @Mapping(source = "status", target = "status", qualifiedByName = "stringToStatus")
    OrdemDeServico toEntity(OrdemDeServicoDTO dto);

    // --- Métodos auxiliares ---
    @Named("statusToString")
    static String mapStatusToString(StatusOrdemServico status) {
        return status != null ? status.name() : null;
    }

    @Named("stringToStatus")
    static StatusOrdemServico mapStringToStatus(String status) {
        if (status == null) {
            return null;
        }
        try {
            return StatusOrdemServico.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Status inválido: '" + status + "'. Valores aceitos: " +
                            Arrays.toString(StatusOrdemServico.values())
            );
        }
    }
}