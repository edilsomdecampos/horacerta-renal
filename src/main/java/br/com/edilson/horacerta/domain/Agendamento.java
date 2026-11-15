package br.com.edilson.horacerta.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "agendamentos")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @Column(name = "tipo_evento", length = 50, nullable = false)
    private String tipoEvento; // 'EXAME', 'CONSULTA', 'FARMACIA_ALTO_CUSTO'

    @Column(length = 255, nullable = false)
    private String descricao;

    @Column(name = "local_destino", length = 150, nullable = false)
    private String localDestino; // 'Hospital Dorim (SP)', 'Sorocaba', etc.

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(name = "precisa_transporte")
    private Boolean precisaTransporte = false;

    @Column(name = "lembrete_enviado")
    private Boolean lembreteEnviado = false;

    @Column(name = "concluido")
    private Boolean concluido = false;

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    public Agendamento() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocalDestino() {
        return localDestino;
    }

    public void setLocalDestino(String localDestino) {
        this.localDestino = localDestino;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Boolean getPrecisaTransporte() {
        return precisaTransporte;
    }

    public void setPrecisaTransporte(Boolean precisaTransporte) {
        this.precisaTransporte = precisaTransporte;
    }

    public Boolean getLembreteEnviado() {
        return lembreteEnviado;
    }

    public void setLembreteEnviado(Boolean lembreteEnviado) {
        this.lembreteEnviado = lembreteEnviado;
    }

    public Boolean getConcluido() {
        return concluido;
    }

    public void setConcluido(Boolean concluido) {
        this.concluido = concluido;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDateTime dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public void marcarComoConcluido() {
        this.concluido = true;
        this.lembreteEnviado = true;
        this.dataConclusao = LocalDateTime.now();
    }
}