package br.com.edilson.horacerta.dto;

import br.com.edilson.horacerta.domain.LocalAdministracao;

public class MedicamentoRequestDTO {

    private String nome;
    private String apresentacao;
    private LocalAdministracao localAdministracao;
    private Integer quantidadeEstoque;
    private String observacoes;

    public MedicamentoRequestDTO() {
    	this.quantidadeEstoque = 0;
    }

    public MedicamentoRequestDTO(String nome, String apresentacao, LocalAdministracao localAdministracao, Integer quantidadeEstoque, String observacoes) {
        this.nome = nome;
        this.apresentacao = apresentacao;
        this.localAdministracao = localAdministracao;
        this.quantidadeEstoque = (quantidadeEstoque != null) ? quantidadeEstoque : 0;
        this.observacoes = observacoes;
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
        this.quantidadeEstoque = (quantidadeEstoque != null) ? quantidadeEstoque : 0;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

   
}
