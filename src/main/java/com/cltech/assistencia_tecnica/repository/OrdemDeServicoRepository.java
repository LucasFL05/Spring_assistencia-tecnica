package com.cltech.assistencia_tecnica.repository;

import com.cltech.assistencia_tecnica.model.OrdemDeServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrdemDeServicoRepository extends JpaRepository<OrdemDeServico, Long> {

    @Query("""
        SELECT COUNT(o) FROM OrdemDeServico o 
        WHERE o.status = 'CONCLUIDA' 
        AND FUNCTION('DATE_TRUNC', 'month', o.dataAbertura) = FUNCTION('DATE_TRUNC', 'month', CURRENT_DATE)
    """)
    long countConcluidasMes();

    @Query("SELECT COUNT(o) FROM OrdemDeServico o WHERE o.status = 'EM_ANDAMENTO'")
    long countEmAndamento();

    @Query("""
        SELECT COUNT(o) FROM OrdemDeServico o 
        WHERE o.status = 'CANCELADA' 
        AND FUNCTION('DATE_TRUNC', 'month', o.dataAbertura) = FUNCTION('DATE_TRUNC', 'month', CURRENT_DATE)
    """)
    long countCanceladasMes();

    @Query("""
        SELECT COUNT(o) FROM OrdemDeServico o 
        WHERE FUNCTION('DATE', o.dataAbertura) = CURRENT_DATE
    """)
    long countAbertasHoje();

    List<OrdemDeServico> findTop10ByOrderByDataAberturaDesc();

    @Query("""
        SELECT o.status, COUNT(o) 
        FROM OrdemDeServico o 
        GROUP BY o.status
    """)
    List<Object[]> countPorStatus();

    // Para essa query, recomendo usar parâmetro, porque CURRENT_DATE - 7 pode falhar:
    @Query("""
        SELECT FUNCTION('DATE', o.dataAbertura), COUNT(o) 
        FROM OrdemDeServico o 
        WHERE o.dataAbertura >= :dataInicio
        GROUP BY FUNCTION('DATE', o.dataAbertura)
        ORDER BY FUNCTION('DATE', o.dataAbertura)
    """)
    List<Object[]> countPorDiaUltimos7(LocalDateTime dataInicio);
}
