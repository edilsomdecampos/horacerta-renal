package br.com.edilson.horacerta.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.edilson.horacerta.domain.LocalAdministracao;
import br.com.edilson.horacerta.dto.MedicamentoRequestDTO;
import br.com.edilson.horacerta.dto.MedicamentoResponseDTO;
import br.com.edilson.horacerta.service.MedicamentoService;

@RestController
@RequestMapping("/api/medicamentos")
public class MedicamentoController {

    private final MedicamentoService service;

    public MedicamentoController(MedicamentoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<MedicamentoResponseDTO>> listar(
            @RequestParam(required = false) LocalAdministracao local) {

        if (local != null) {
            return ResponseEntity.ok(service.listarPorLocal(local));
        }
        return ResponseEntity.ok(service.listarTodos());
    }

    @PostMapping
    public ResponseEntity<MedicamentoResponseDTO> cadastrar(@RequestBody MedicamentoRequestDTO dto) {
        MedicamentoResponseDTO criado = service.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicamentoResponseDTO> atualizar(
            @PathVariable UUID id, 
            @RequestBody MedicamentoRequestDTO dto) {
        
        MedicamentoResponseDTO atualizado = service.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @PatchMapping("/{id}/confirmar-dose")
    public ResponseEntity<MedicamentoResponseDTO> confirmarDose(@PathVariable UUID id) {
        MedicamentoResponseDTO atualizado = service.confirmarDose(id);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}