package com.cltech.assistencia_tecnica.dto;

import com.cltech.assistencia_tecnica.model.Dispositivo;
import com.cltech.assistencia_tecnica.model.OrdemDeServico;
import com.cltech.assistencia_tecnica.service.DispositivoService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.modelmapper.ModelMapper;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Data
public class OrdemDeServicoDTO {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "A descrição do problema é obrigatória")
    private String descricaoProblema;

    @NotBlank(message = "O status é obrigatório")
    private String status;

    @NotNull(message = "A data de abertura é obrigatória")
    private LocalDateTime dataAbertura;

    private LocalDateTime dataConclusao;

    @NotNull(message = "O dispositivo associado é obrigatório")
    private Long dispositivoId;

    public OrdemDeServico toEntity(DispositivoService dispositivoService) {
        ModelMapper modelMapper = new ModelMapper();
        OrdemDeServico ordemDeServico = modelMapper.map(this, OrdemDeServico.class);
        ordemDeServico.setDispositivo(new ModelMapper().map(dispositivoService.buscarDispositivoPorId(this.getDispositivoId()), Dispositivo.class));
        return ordemDeServico;
    }
}