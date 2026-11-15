package br.com.edilson.horacerta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.edilson.horacerta.domain.Agendamento;
import br.com.edilson.horacerta.infrastructure.AgendamentoRepository;

@RestController
@RequestMapping("/api/agendamentos")
@CrossOrigin(origins = "*")
public class AgendamentoController {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @GetMapping
    public List<Agendamento> listarTodos() {
        return agendamentoRepository.findAll();
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<Agendamento> listarPorPaciente(@PathVariable Long pacienteId) {
        return agendamentoRepository.findByPacienteId(pacienteId);
    }

    @PostMapping
    public ResponseEntity<Agendamento> salvar(@RequestBody Agendamento agendamento) {
        if (agendamento.getConcluido() == null) {
            agendamento.setConcluido(false);
        }
        Agendamento salvo = agendamentoRepository.save(agendamento);
        return ResponseEntity.ok(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agendamento> atualizar(@PathVariable Long id, @RequestBody Agendamento dados) {
        return agendamentoRepository.findById(id)
                .map(existente -> {
                    existente.setTipoEvento(dados.getTipoEvento());
                    existente.setDescricao(dados.getDescricao());
                    existente.setLocalDestino(dados.getLocalDestino());
                    existente.setDataHora(dados.getDataHora());
                    existente.setPrecisaTransporte(dados.getPrecisaTransporte());
                    existente.setObservacoes(dados.getObservacoes());
                    if (dados.getConcluido() != null) {
                        existente.setConcluido(dados.getConcluido());
                    }
                    return ResponseEntity.ok(agendamentoRepository.save(existente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<Agendamento> concluir(@PathVariable Long id) {
        return agendamentoRepository.findById(id)
                .map(agendamento -> {
                    agendamento.marcarComoConcluido();
                    return ResponseEntity.ok(agendamentoRepository.save(agendamento));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!agendamentoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        agendamentoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}