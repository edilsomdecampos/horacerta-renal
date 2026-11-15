package br.com.edilson.horacerta.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "medicamentos")
@SQLRestriction("ativo = true")
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 100)
    private String apresentacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "local_administracao", nullable = false, length = 30)
    private LocalAdministracao localAdministracao;

    @Column(name = "quantidade_estoque", nullable = false)
    private Integer quantidadeEstoque = 0;

    @Column(length = 500)
    private String observacoes;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm = LocalDateTime.now();

    @Column(name = "ultima_confirmacao")
    private LocalDateTime ultimaConfirmacao;

    public Medicamento() {
    }

    public Medicamento(String nome, String apresentacao, LocalAdministracao localAdministracao) {
        this.nome = nome;
        this.apresentacao = apresentacao;
        this.localAdministracao = localAdministracao;
        this.quantidadeEstoque = 0;
        this.observacoes = null;
        this.ativo = true;
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
    }

    public Medicamento(String nome, String apresentacao, LocalAdministracao localAdministracao, Integer quantidadeEstoque, String observacoes) {
        this.nome = nome;
        this.apresentacao = apresentacao;
        this.localAdministracao = localAdministracao;
        this.quantidadeEstoque = (quantidadeEstoque != null) ? quantidadeEstoque : 0;
        this.observacoes = observacoes;
        this.ativo = true;
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
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
        this.atualizadoEm = LocalDateTime.now();
    }

    public String getApresentacao() {
        return apresentacao;
    }

    public void setApresentacao(String apresentacao) {
        this.apresentacao = apresentacao;
        this.atualizadoEm = LocalDateTime.now();
    }

    public LocalAdministracao getLocalAdministracao() {
        return localAdministracao;
    }

    public void setLocalAdministracao(LocalAdministracao localAdministracao) {
        this.localAdministracao = localAdministracao;
        this.atualizadoEm = LocalDateTime.now();
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = (quantidadeEstoque != null) ? quantidadeEstoque : 0;
        this.atualizadoEm = LocalDateTime.now();
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
        this.atualizadoEm = LocalDateTime.now();
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

    public LocalDateTime getUltimaConfirmacao() {
        return ultimaConfirmacao;
    }

    public void setUltimaConfirmacao(LocalDateTime ultimaConfirmacao) {
        this.ultimaConfirmacao = ultimaConfirmacao;
    }

    public void desativar() {
        this.ativo = false;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void decrementarEstoque(int quantidade) {
        if (this.quantidadeEstoque != null && this.quantidadeEstoque >= quantidade) {
            this.quantidadeEstoque -= quantidade;
        } else {
            this.quantidadeEstoque = 0;
        }
        LocalDateTime agora = LocalDateTime.now();
        this.ultimaConfirmacao = agora;
        this.atualizadoEm = agora;
    }
}