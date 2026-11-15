package br.com.edilson.horacerta.service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.edilson.horacerta.domain.LocalAdministracao;
import br.com.edilson.horacerta.domain.Medicamento;
import br.com.edilson.horacerta.domain.Paciente;
import br.com.edilson.horacerta.dto.MedicamentoRequestDTO;
import br.com.edilson.horacerta.dto.MedicamentoResponseDTO;
import br.com.edilson.horacerta.infrastructure.MedicamentoRepository;
import br.com.edilson.horacerta.infrastructure.PacienteRepository;

@Service
public class MedicamentoService {

    private final MedicamentoRepository repository;
    private final PacienteRepository pacienteRepository;
    private final WhatsAppService whatsAppService;

    public MedicamentoService(
            MedicamentoRepository repository,
            PacienteRepository pacienteRepository,
            WhatsAppService whatsAppService) {
        this.repository = repository;
        this.pacienteRepository = pacienteRepository;
        this.whatsAppService = whatsAppService;
    }

    public List<MedicamentoResponseDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(MedicamentoResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<MedicamentoResponseDTO> listarPorLocal(LocalAdministracao local) {
        return repository.findByLocalAdministracao(local)
                .stream()
                .map(MedicamentoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public MedicamentoResponseDTO cadastrar(MedicamentoRequestDTO dto) {
        Medicamento medicamento = new Medicamento(
            dto.getNome(),
            dto.getApresentacao(),
            dto.getLocalAdministracao(),
            dto.getQuantidadeEstoque(),
            dto.getObservacoes()
        );

        Medicamento salvo = repository.save(medicamento);
        return new MedicamentoResponseDTO(salvo);
    }

    @Transactional
    public MedicamentoResponseDTO atualizar(UUID id, MedicamentoRequestDTO dto) {
        Medicamento medicamento = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicamento não encontrado com o ID: " + id));

        medicamento.setNome(dto.getNome());
        medicamento.setApresentacao(dto.getApresentacao());
        medicamento.setLocalAdministracao(dto.getLocalAdministracao());
        medicamento.setQuantidadeEstoque(dto.getQuantidadeEstoque());
        medicamento.setObservacoes(dto.getObservacoes());

        Medicamento atualizado = repository.save(medicamento);
        return new MedicamentoResponseDTO(atualizado);
    }

    @Transactional
    public MedicamentoResponseDTO confirmarDose(UUID id) {
        Medicamento medicamento = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicamento não encontrado com o ID: " + id));

        if (medicamento.getQuantidadeEstoque() == null || medicamento.getQuantidadeEstoque() <= 0) {
            throw new RuntimeException("Estoque zerado para o medicamento: " + medicamento.getNome());
        }

        // Baixa o estoque e grava automaticamente a data/hora em ultimaConfirmacao
        medicamento.decrementarEstoque(1);
        Medicamento atualizado = repository.save(medicamento);

        // Dispara recibo de confirmação no WhatsApp com emojis blindados (UTF-8)
        List<Paciente> pacientes = pacienteRepository.findAll();
        if (!pacientes.isEmpty()) {
            Paciente paciente = pacientes.get(0);
            String horaConfirmacao = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));

            String mensagem = String.format(
                "\u2705 *HoraCerta Renal - Dose Confirmada*\n" +
                "\uD83D\uDC64 *Paciente:* %s\n" +
                "\uD83D\uDC8A *Medicamento:* %s\n" +
                "\uD83D\uDD52 *Horário:* %s\n" +
                "\uD83D\uDCE6 *Estoque Atualizado:* %d doses",
                paciente.getNome(),
                atualizado.getNome(),
                horaConfirmacao,
                atualizado.getQuantidadeEstoque()
            );

            whatsAppService.enviarMensagem(paciente.getTelefonePaciente(), mensagem);
            whatsAppService.enviarMensagem(paciente.getTelefoneAcompanhante(), mensagem);
        }

        return new MedicamentoResponseDTO(atualizado);
    }

    @Transactional
    public void excluir(UUID id) {
        Medicamento medicamento = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicamento não encontrado com o ID: " + id));
        
        medicamento.desativar();
        repository.save(medicamento);
    }
}