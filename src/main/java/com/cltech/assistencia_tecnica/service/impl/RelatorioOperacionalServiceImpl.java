package com.cltech.assistencia_tecnica.service.impl;

import com.cltech.assistencia_tecnica.dto.OrdemResumoDTO;
import com.cltech.assistencia_tecnica.dto.RelatorioOperacionalDTO;
import com.cltech.assistencia_tecnica.model.Dispositivo;
import com.cltech.assistencia_tecnica.model.OrdemDeServico;
import com.cltech.assistencia_tecnica.repository.OrdemDeServicoRepository;
import com.cltech.assistencia_tecnica.service.RelatorioOperacionalService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RelatorioOperacionalServiceImpl implements RelatorioOperacionalService {

    private final OrdemDeServicoRepository repository;

    public RelatorioOperacionalServiceImpl(OrdemDeServicoRepository repository) {
        this.repository = repository;
    }

    @Override
    public RelatorioOperacionalDTO gerarRelatorio() {
        long totalConcluidasMes = repository.countConcluidasMes();
        long totalEmAndamento   = repository.countEmAndamento();
        long totalCanceladasMes = repository.countCanceladasMes();
        long totalAbertasHoje   = repository.countAbertasHoje();

        List<OrdemResumoDTO> ordensRecentes = repository.findTop10ByOrderByDataAberturaDesc()
                .stream()
                .map(ordem -> new OrdemResumoDTO(
                        ordem.getId(),
                        getNomeCliente(ordem),
                        getDescricaoDispositivo(ordem),
                        ordem.getStatus().name(),
                        ordem.getDataAbertura()
                ))
                .toList();

        Map<String, Long> distribuicaoPorStatus = new LinkedHashMap<>();
        List<Object[]> statusData = repository.countPorStatus();
        for (Object[] row : statusData) {
            String status = row[0] != null ? row[0].toString() : "N/A";
            Long count    = row[1] != null ? ((Number) row[1]).longValue() : 0L;
            distribuicaoPorStatus.put(status, count);
        }

        Map<String, Long> ordensPorDia = new LinkedHashMap<>();
        LocalDateTime dataInicio = LocalDateTime.now().minusDays(7);

        List<Object[]> diaData = repository.countPorDiaUltimos7(dataInicio);

        for (Object[] row : diaData) {
            LocalDate data = row[0] instanceof java.sql.Date
                    ? ((java.sql.Date) row[0]).toLocalDate()
                    : (LocalDate) row[0];
            Long count = row[1] != null ? ((Number) row[1]).longValue() : 0L;
            ordensPorDia.put(data.toString(), count);
        }


        return new RelatorioOperacionalDTO(
                totalConcluidasMes,
                totalEmAndamento,
                totalCanceladasMes,
                totalAbertasHoje,
                ordensRecentes,
                distribuicaoPorStatus,
                ordensPorDia
        );
    }

    private String getNomeCliente(OrdemDeServico ordem) {
        return Optional.ofNullable(ordem.getDispositivo())
                .map(Dispositivo::getCliente)
                .map(cliente -> cliente.getNome())
                .orElse("N/A");
    }

    private String getDescricaoDispositivo(OrdemDeServico ordem) {
        return Optional.ofNullable(ordem.getDispositivo())
                .map(d -> d.getMarca() + " " + d.getModelo())
                .orElse("N/A");
    }
}
