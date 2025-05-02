package com.cltech.assistencia_tecnica.service.impl;

import com.cltech.assistencia_tecnica.dto.OrdemDeServicoDTO;
import com.cltech.assistencia_tecnica.mapper.OrdemDeServicoMapper;
import com.cltech.assistencia_tecnica.model.Dispositivo;
import com.cltech.assistencia_tecnica.model.OrdemDeServico;
import com.cltech.assistencia_tecnica.model.StatusOrdemServico;
import com.cltech.assistencia_tecnica.repository.OrdemDeServicoRepository;
import com.cltech.assistencia_tecnica.service.DispositivoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdemDeServicoServiceImplTest {

    @Mock
    private OrdemDeServicoRepository ordemDeServicoRepository;

    @Mock
    private OrdemDeServicoMapper ordemDeServicoMapper;

    @Mock
    private DispositivoService dispositivoService;

    @InjectMocks
    private OrdemDeServicoServiceImpl ordemDeServicoService;

    @Captor
    private ArgumentCaptor<OrdemDeServico> ordemCaptor;

    @Test
    void createOrder_ShouldReturnCreatedOrder() {
        Long deviceId = 1L;
        Long orderId = 1L;

        OrdemDeServicoDTO inputDto = new OrdemDeServicoDTO();
        inputDto.setDispositivoId(deviceId);

        Dispositivo device = new Dispositivo();
        device.setId(deviceId);

        OrdemDeServico orderToSave = new OrdemDeServico();
        OrdemDeServico savedOrder = new OrdemDeServico();
        savedOrder.setId(orderId);
        savedOrder.setDispositivo(device);

        OrdemDeServicoDTO expectedDto = new OrdemDeServicoDTO();
        expectedDto.setId(orderId);
        expectedDto.setDispositivoId(deviceId);

        when(ordemDeServicoMapper.toEntity(inputDto)).thenReturn(orderToSave);
        when(dispositivoService.buscarEntidadeDispositivoPorId(deviceId)).thenReturn(device);
        when(ordemDeServicoRepository.save(orderToSave)).thenReturn(savedOrder);
        when(ordemDeServicoMapper.toDto(savedOrder)).thenReturn(expectedDto);

        OrdemDeServicoDTO result = ordemDeServicoService.criarOrdemDeServico(inputDto);

        assertNotNull(result);
        assertEquals(orderId, result.getId());
        verify(ordemDeServicoRepository).save(ordemCaptor.capture());
        OrdemDeServico capturedOrder = ordemCaptor.getValue();
        assertNotNull(capturedOrder.getDispositivo());
        assertEquals(deviceId, capturedOrder.getDispositivo().getId());
    }

    @Test
    void getOrderById_WhenExists_ShouldReturnOrder() {
        Long id = 1L;
        OrdemDeServico order = new OrdemDeServico();
        order.setId(id);
        OrdemDeServicoDTO dto = new OrdemDeServicoDTO();
        dto.setId(id);

        when(ordemDeServicoRepository.findById(id)).thenReturn(Optional.of(order));
        when(ordemDeServicoMapper.toDto(order)).thenReturn(dto);

        OrdemDeServicoDTO result = ordemDeServicoService.buscarOrdemPorId(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void getOrderById_WhenNotExists_ShouldThrowException() {
        Long id = 1L;
        when(ordemDeServicoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ordemDeServicoService.buscarOrdemPorId(id));
    }

    @Test
    void listOrders_WhenOrdersExist_ShouldReturnList() {
        OrdemDeServico entity = new OrdemDeServico();
        OrdemDeServicoDTO dto = new OrdemDeServicoDTO();

        when(ordemDeServicoRepository.findAll()).thenReturn(Collections.singletonList(entity));
        when(ordemDeServicoMapper.toDto(entity)).thenReturn(dto);

        List<OrdemDeServicoDTO> result = ordemDeServicoService.listarOrdens();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void listOrders_WhenNoOrdersExist_ShouldReturnEmptyList() {
        when(ordemDeServicoRepository.findAll()).thenReturn(Collections.emptyList());

        List<OrdemDeServicoDTO> result = ordemDeServicoService.listarOrdens();

        assertTrue(result.isEmpty());
    }

    @Test
    void updateStatus_WhenValidStatus_ShouldUpdateAndReturnOrder() {
        Long id = 1L;
        String newStatus = "EM_ANDAMENTO";

        OrdemDeServico order = new OrdemDeServico();
        order.setId(id);
        order.setStatus(StatusOrdemServico.ABERTA);

        OrdemDeServicoDTO dto = new OrdemDeServicoDTO();
        dto.setId(id);
        dto.setStatus(newStatus);

        when(ordemDeServicoRepository.findById(id)).thenReturn(Optional.of(order));
        when(ordemDeServicoRepository.save(ordemCaptor.capture())).thenReturn(order);
        when(ordemDeServicoMapper.toDto(order)).thenReturn(dto);

        OrdemDeServicoDTO result = ordemDeServicoService.atualizarStatus(id, newStatus);

        OrdemDeServico savedOrder = ordemCaptor.getValue();
        assertEquals(StatusOrdemServico.EM_ANDAMENTO, savedOrder.getStatus());
        assertEquals(newStatus, result.getStatus());
    }

    @Test
    void updateStatus_WhenInvalidStatus_ShouldThrowException() {
        Long id = 1L;
        String invalidStatus = "INVALID_STATUS";

        OrdemDeServico order = new OrdemDeServico();
        order.setId(id);

        when(ordemDeServicoRepository.findById(id)).thenReturn(Optional.of(order));

        assertThrows(IllegalArgumentException.class,
                () -> ordemDeServicoService.atualizarStatus(id, invalidStatus));
    }

    @Test
    void updateStatus_WhenOrderNotFound_ShouldThrowException() {
        Long id = 1L;
        String newStatus = "FINALIZADA";

        when(ordemDeServicoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> ordemDeServicoService.atualizarStatus(id, newStatus));
    }

    @Test
    void deleteOrder_WhenExists_ShouldDelete() {
        Long id = 1L;
        when(ordemDeServicoRepository.existsById(id)).thenReturn(true);

        ordemDeServicoService.deletarOrdem(id);

        verify(ordemDeServicoRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteOrder_WhenNotExists_ShouldThrowException() {
        Long id = 1L;
        when(ordemDeServicoRepository.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> ordemDeServicoService.deletarOrdem(id));
        verify(ordemDeServicoRepository, never()).deleteById(anyLong());
    }
}
