package com.cltech.assistencia_tecnica.controller;

import com.cltech.assistencia_tecnica.dto.ClienteDTO;
import com.cltech.assistencia_tecnica.model.Cliente;
import com.cltech.assistencia_tecnica.service.ClienteService;
import com.cltech.assistencia_tecnica.mapper.ClienteMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Cliente", description = "Endpoints for managing clientes")
public class ClienteController {

    private final ClienteService clienteService;

    private final ClienteMapper clienteMapper;

    public ClienteController(ClienteService clienteService, ClienteMapper clienteMapper) {
        this.clienteService = clienteService;
        this.clienteMapper = clienteMapper;
    }

    @PostMapping
    @Operation(summary = "Create a new cliente")
    public ResponseEntity<ClienteDTO> criarCliente(
            @Valid @RequestBody ClienteDTO dto) {

        Cliente criado = clienteService.criarCliente(
                clienteMapper.clienteDTOToCliente(dto));

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(criado.getId())
                .toUri();

        return ResponseEntity
                .created(uri)
                .body(clienteMapper.clienteToClienteDTO(criado));
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get a cliente by ID")
    public ResponseEntity<ClienteDTO> buscarClientePorId(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarClientePorId(id);
        return ResponseEntity.ok(clienteMapper.clienteToClienteDTO(cliente));
    }

    @GetMapping
    @Operation(summary = "List all clientes")
    public ResponseEntity<List<ClienteDTO>> listarClientes() {
        List<Cliente> clientes = clienteService.listarClientes();
        List<ClienteDTO> clientesDTO = clientes.stream()
                .map(clienteMapper::clienteToClienteDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientesDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a cliente by ID")
    public ResponseEntity<ClienteDTO> atualizarCliente(@PathVariable Long id, @Valid @RequestBody ClienteDTO clienteDTO) {
        Cliente clienteAtualizado = clienteMapper.clienteDTOToCliente(clienteDTO);
        Cliente cliente = clienteService.atualizarCliente(id, clienteAtualizado);
        return ResponseEntity.ok(clienteMapper.clienteToClienteDTO(cliente));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a cliente by ID")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        clienteService.deletarCliente(id);
        return ResponseEntity.noContent().build();
    }
}
