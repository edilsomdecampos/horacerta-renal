# 🩺 HoraCerta Renal — Hub Clínico & Automação de Hemodiálise

Sistema Full-Stack desenvolvido para gerenciamento clínico e operacional da rotina de pacientes em tratamento de hemodiálise, unindo **controle transacional de estoque medicamentoso**, **motor de vigilância em tempo real** e **notificações automatizadas via WhatsApp**.

---

## 🎯 Problema Resolvido & Impacto Real
Pacientes renais crônicos e acompanhantes lidam diariamente com múltiplas janelas de medicação (domiciliar e intradialítica), restrições de horários pós-refeição e retiradas mensais na Farmácia de Alto Custo. O **HoraCerta Renal** elimina falhas humanas e sobrecarga cognitiva através de automação determinística e rastreabilidade completa.

---

## 🏗️ Arquitetura & Stack Tecnológica

* **Back-end:** Java 21, Spring Boot 3, Spring Data JPA, Spring Scheduler (`@Scheduled`)
* **Banco de Dados:** PostgreSQL (persistência relacional e controle transacional de saldo)
* **Front-end:** HTML5, CSS3 (Glassmorphism UI), JavaScript Vanilla, Web Audio API nativa (alertas sonoros de baixa latência sem dependências externas)
* **Integração Mensageria:** API REST de WhatsApp (disparo automático de lembretes, catch-up de doses pendentes e recibos de baixa de estoque)

---

## ⚡ Principais Funcionalidades

* **Motor de Vigilância & Catch-up (`AlertaSchedulerService`):** Monitoramento contínuo de janelas de horários (8h, 12h, 24h e ciclos alimentares). Ao inicializar o servidor, o sistema varre doses pendentes do dia e envia um resumo consolidado no WhatsApp.
* **Gestão Transacional de Estoque (`MedicamentoService`):** Baixa atômica em 1 clique separada por **Uso Doméstico** e **Aplicação na Clínica (Hemodiálise)**, com bloqueio de saldo negativo, alerta preventivo de estoque baixo e comprovante instantâneo no WhatsApp.
* **Agenda Clínica & Histórico de Adesão:** Registro visual de doses aplicadas, horários programados e controle de retiradas na Farmácia de Alto Custo.
* **Segurança & Blindagem:** Isolamento completo de credenciais via variáveis de ambiente e `.gitignore`.

---

## 🚀 Como Executar Localmente

1. **Clone o repositório:**
   `git clone [https://github.com/edilsomdecampos/horacerta-renal.git](https://github.com/edilsomdecampos/horacerta-renal.git)`

2. **Configure as variáveis de ambiente:**
   Copie o arquivo `src/main/resources/application-example.properties`, renomeie para `application.properties` e preencha sua senha do PostgreSQL e a chave da API do WhatsApp.

3. **Execute a aplicação:**
   Rode a classe `HoraCertaApplication.java` via Eclipse/IDE ou via Maven (`./mvnw spring-boot:run`).

4. **Acesse o painel:**
   Abra `http://localhost:8081` no navegador.

---

## 👨‍💻 Autor
Desenvolvido por **Edilsom de Campos** — Engenheiro de Software Back-end (Java / Spring Boot / PostgreSQL).
