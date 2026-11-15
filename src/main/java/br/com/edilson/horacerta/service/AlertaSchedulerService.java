package br.com.edilson.horacerta.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.edilson.horacerta.domain.Agendamento;
import br.com.edilson.horacerta.domain.LocalAdministracao;
import br.com.edilson.horacerta.domain.Medicamento;
import br.com.edilson.horacerta.domain.Paciente;
import br.com.edilson.horacerta.infrastructure.AgendamentoRepository;
import br.com.edilson.horacerta.infrastructure.MedicamentoRepository;
import br.com.edilson.horacerta.infrastructure.PacienteRepository;

@Service
public class AlertaSchedulerService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private WhatsAppService whatsAppService;

    private final Set<String> alertasMedicamentosDisparados = new HashSet<>();
    private String diaAtualControle = "";
    private final Pattern patternColchetes = Pattern.compile("\\[(.*?)\\]");

    // RODA UMA ÚNICA VEZ ASSIM QUE VOCÊ LIGA O NOTEBOOK E SOBE O PROJETO
    @EventListener(ApplicationReadyEvent.class)
    public void verificarDosesPerdidasAoIniciar() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDate hoje = agora.toLocalDate();
        LocalTime horaAtual = agora.toLocalTime();

        List<Paciente> pacientes = pacienteRepository.findAll();
        if (pacientes.isEmpty()) return;

        Paciente paciente = pacientes.get(0);
        List<Medicamento> medicamentos = medicamentoRepository.findAll();
        List<String> pendencias = new ArrayList<>();

        for (Medicamento med : medicamentos) {
            if (med.getLocalAdministracao() != LocalAdministracao.USO_DOMESTICO) {
                continue;
            }

            // Se já houve confirmação hoje após o horário previsto, ignora
            boolean jaConfirmadoHoje = med.getUltimaConfirmacao() != null
                    && med.getUltimaConfirmacao().toLocalDate().equals(hoje);

            String posologia = med.getApresentacao() != null ? med.getApresentacao() : "";
            Matcher matcher = patternColchetes.matcher(posologia);

            if (matcher.find()) {
                String[] horarios = matcher.group(1).split(",");
                for (String h : horarios) {
                    try {
                        LocalTime horarioDose = LocalTime.parse(h.trim(), DateTimeFormatter.ofPattern("HH:mm"));
                        
                        // Verifica se o horário já passou hoje e não foi confirmado
                        if (horarioDose.isBefore(horaAtual)) {
                            boolean confirmadoDepoisDaDose = jaConfirmadoHoje
                                    && !med.getUltimaConfirmacao().toLocalTime().isBefore(horarioDose);

                            if (!confirmadoDepoisDaDose) {
                                String chaveUnica = hoje + "-" + med.getId() + "-" + h.trim();
                                alertasMedicamentosDisparados.add(chaveUnica);

                                int estoque = med.getQuantidadeEstoque() != null ? med.getQuantidadeEstoque() : 0;
                                pendencias.add(String.format("• *%s* - %s _(Estoque: %d doses)_",
                                        h.trim(), med.getNome(), estoque));
                            }
                        }
                    } catch (Exception e) {
                        // Ignora formato de hora inválido dentro dos colchetes
                    }
                }
            }
        }

        // Se encontrou doses da manhã enquanto o notebook estava desligado, manda 1 resumo consolidado
        if (!pendencias.isEmpty()) {
            String mensagem = String.format(
                "⚠️ *HoraCerta Renal - Doses Pendentes de Hoje*\n" +
                "👤 *Paciente:* %s\n" +
                "Identificamos horários que passaram enquanto o sistema estava offline:\n\n" +
                "%s\n\n" +
                "_O estoque permanece intacto. Confirme no painel assim que tomar para dar baixa._",
                paciente.getNome(),
                String.join("\n", pendencias)
            );

            whatsAppService.enviarMensagem(paciente.getTelefonePaciente(), mensagem);
            whatsAppService.enviarMensagem(paciente.getTelefoneAcompanhante(), mensagem);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void executarVigilanciaRotina() {
        verificarHorariosMedicamentos();
        verificarCompromissosProximos();
    }

    private void verificarHorariosMedicamentos() {
        LocalDateTime agora = LocalDateTime.now();
        String hoje = agora.toLocalDate().toString();

        if (!hoje.equals(diaAtualControle)) {
            alertasMedicamentosDisparados.clear();
            diaAtualControle = hoje;
        }

        String horaMinutoAtual = agora.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        List<Paciente> pacientes = pacienteRepository.findAll();
        if (pacientes.isEmpty()) return;

        Paciente paciente = pacientes.get(0);
        List<Medicamento> medicamentos = medicamentoRepository.findAll();

        for (Medicamento med : medicamentos) {
            if (med.getLocalAdministracao() != LocalAdministracao.USO_DOMESTICO) {
                continue;
            }

            String posologia = med.getApresentacao() != null ? med.getApresentacao() : "";
            Matcher matcher = patternColchetes.matcher(posologia);

            boolean horaDeTomar = false;
            if (matcher.find()) {
                String[] horarios = matcher.group(1).split(",");
                for (String h : horarios) {
                    if (h.trim().equals(horaMinutoAtual)) {
                        horaDeTomar = true;
                        break;
                    }
                }
            } else if (posologia.contains(horaMinutoAtual)) {
                horaDeTomar = true;
            }

            if (horaDeTomar) {
                String chaveUnica = hoje + "-" + med.getId() + "-" + horaMinutoAtual;
                if (!alertasMedicamentosDisparados.contains(chaveUnica)) {
                    alertasMedicamentosDisparados.add(chaveUnica);

                    int estoque = med.getQuantidadeEstoque() != null ? med.getQuantidadeEstoque() : 0;
                    String avisoEstoque = (estoque <= 5)
                        ? "\n⚠️ *ALERTA DE ESTOQUE:* Restam apenas *" + estoque + "* doses!"
                        : "\n📦 *Estoque atual:* " + estoque + " doses";

                    String mensagem = String.format(
                        "⏰ *HoraCerta Renal - Hora do Medicamento*\n" +
                        "👤 *Paciente:* %s\n" +
                        "💊 *Medicamento:* %s\n" +
                        "📋 *Posologia:* %s\n" +
                        "🕒 *Horário:* %s%s\n\n" +
                        "_Após tomar, confirme no painel para dar baixa no estoque._",
                        paciente.getNome(),
                        med.getNome(),
                        posologia,
                        horaMinutoAtual,
                        avisoEstoque
                    );

                    whatsAppService.enviarMensagem(paciente.getTelefonePaciente(), mensagem);
                    whatsAppService.enviarMensagem(paciente.getTelefoneAcompanhante(), mensagem);
                }
            }
        }
    }

    public void verificarCompromissosProximos() {
        LocalDateTime agora = LocalDateTime.now();
        List<Agendamento> todos = agendamentoRepository.findAll();

        for (Agendamento ag : todos) {
            boolean naoEnviado = ag.getLembreteEnviado() == null || !ag.getLembreteEnviado();
            if (!naoEnviado || ag.getDataHora().isBefore(agora)) {
                continue;
            }

            String tipo = String.valueOf(ag.getTipoEvento()).toUpperCase();
            String destino = ag.getLocalDestino() != null ? ag.getLocalDestino().toUpperCase() : "";
            String desc = ag.getDescricao() != null ? ag.getDescricao().toUpperCase() : "";

            boolean isAltoCusto = "FARMACIA_ALTO_CUSTO".equals(tipo);
            boolean isHrimSp = destino.contains("RIM") || destino.contains("HRIM") || desc.contains("HRIM") || destino.contains("SÃO PAULO") || destino.contains("SP");

            LocalDateTime limiteAlerta = (isAltoCusto || isHrimSp) ? agora.plusHours(24) : agora.plusHours(2);

            if (ag.getDataHora().isBefore(limiteAlerta)) {
                Paciente paciente = ag.getPaciente();
                String dataFormatada = ag.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm"));

                String checklistExtra = "";
                if (isAltoCusto) {
                    checklistExtra = "\n📋 *Checklist Alto Custo:* Separar RG, Cartão SUS, Receita Médica e LME atualizada.";
                } else if (isHrimSp) {
                    checklistExtra = "\n🏥 *Viagem HRim (SP):* Conferir horário de saída, exames recentes e documentos pessoais.";
                }

                String transporte = Boolean.TRUE.equals(ag.getPrecisaTransporte())
                    ? "\n🚐 *Transporte:* Transporte Sanitário Municipal Solicitado"
                    : "";

                String mensagem = String.format(
                    "🗓️ *HoraCerta Renal - Lembrete de Agenda*\n" +
                    "👤 *Paciente:* %s\n" +
                    "📌 *Evento:* %s (%s)\n" +
                    "📍 *Local:* %s\n" +
                    "🕒 *Data/Hora:* %s%s%s",
                    paciente.getNome(),
                    ag.getDescricao(),
                    ag.getTipoEvento(),
                    ag.getLocalDestino(),
                    dataFormatada,
                    transporte,
                    checklistExtra
                );

                whatsAppService.enviarMensagem(paciente.getTelefonePaciente(), mensagem);
                whatsAppService.enviarMensagem(paciente.getTelefoneAcompanhante(), mensagem);

                ag.setLembreteEnviado(true);
                agendamentoRepository.save(ag);
            }
        }
    }
}