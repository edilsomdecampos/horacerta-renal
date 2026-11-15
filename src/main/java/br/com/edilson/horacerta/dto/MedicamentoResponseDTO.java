package br.com.edilson.horacerta.dto;

import br.com.edilson.horacerta.domain.LocalAdministracao;
import br.com.edilson.horacerta.domain.Medicamento;

import java.time.LocalDateTime;
import java.util.UUID;

public class MedicamentoResponseDTO {

    private UUID id;
    private String nome;
    private String apresentacao;
    private LocalAdministracao localAdministracao;
    private Integer quantidadeEstoque;
    private String observacoes;
    private Boolean ativo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public MedicamentoResponseDTO() {
    }

    public MedicamentoResponseDTO(Medicamento medicamento) {
        this.id = medicamento.getId();
        this.nome = medicamento.getNome();
        this.apresentacao = medicamento.getApresentacao();
        this.localAdministracao = medicamento.getLocalAdministracao();
        this.quantidadeEstoque = medicamento.getQuantidadeEstoque();
        this.observacoes = medicamento.getObservacoes();
        this.ativo = medicamento.getAtivo();
        this.criadoEm = medicamento.getCriadoEm();
        this.atualizadoEm = medicamento.getAtualizadoEm();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApresentacao() {
        return apresentacao;
    }

    public void setApresentacao(String apresentacao) {
        this.apresentacao = apresentacao;
    }

    public LocalAdministracao getLocalAdministracao() {
        return localAdministracao;
    }

    public void setLocalAdministracao(LocalAdministracao localAdministracao) {
        this.localAdministracao = localAdministracao;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }
}