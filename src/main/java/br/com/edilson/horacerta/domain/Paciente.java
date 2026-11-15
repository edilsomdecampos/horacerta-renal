package br.com.edilson.horacerta.domain; // Ajuste se o seu pacote tiver outro prefixo

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 150)
    private String nomeAcompanhante;

    @Column(nullable = false, length = 20)
    private String telefonePaciente;

    @Column(nullable = false, length = 20)
    private String telefoneAcompanhante;

    @Column(columnDefinition = "TEXT")
    private String observacoesClinicas;

    // Construtor padrão exigido pelo JPA
    public Paciente() {
    }

    // Construtor completo
    public Paciente(Long id, String nome, String nomeAcompanhante, String telefonePaciente, 
                    String telefoneAcompanhante, String observacoesClinicas) {
        this.id = id;
        this.nome = nome;
        this.nomeAcompanhante = nomeAcompanhante;
        this.telefonePaciente = telefonePaciente;
        this.telefoneAcompanhante = telefoneAcompanhante;
        this.observacoesClinicas = observacoesClinicas;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeAcompanhante() {
        return nomeAcompanhante;
    }

    public void setNomeAcompanhante(String nomeAcompanhante) {
        this.nomeAcompanhante = nomeAcompanhante;
    }

    public String getTelefonePaciente() {
        return telefonePaciente;
    }

    public void setTelefonePaciente(String telefonePaciente) {
        this.telefonePaciente = telefonePaciente;
    }

    public String getTelefoneAcompanhante() {
        return telefoneAcompanhante;
    }

    public void setTelefoneAcompanhante(String telefoneAcompanhante) {
        this.telefoneAcompanhante = telefoneAcompanhante;
    }

    public String getObservacoesClinicas() {
        return observacoesClinicas;
    }

    public void setObservacoesClinicas(String observacoesClinicas) {
        this.observacoesClinicas = observacoesClinicas;
    }
}