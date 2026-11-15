package br.com.edilson.horacerta.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WhatsAppService {

    @Value("${evolution.api.url}")
    private String apiUrl;

    @Value("${evolution.api.key}")
    private String apiKey;

    @Value("${evolution.api.instance}")
    private String instanceName;

    private final RestClient restClient = RestClient.create();

    public void enviarMensagem(String telefone, String texto) {
        if (telefone == null || telefone.isBlank()) {
            return;
        }

        String numeroLimpo = telefone.replaceAll("\\D", "");
        if (!numeroLimpo.startsWith("55")) {
            numeroLimpo = "55" + numeroLimpo;
        }

        String endpoint = String.format("%s/message/sendText/%s", apiUrl, instanceName);

        Map<String, Object> payload = Map.of(
            "number", numeroLimpo,
            "text", texto
        );

        try {
            restClient.post()
                .uri(endpoint)
                .header("apikey", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload)
                .retrieve()
                .toBodilessEntity();

            System.out.println("✅ [WHATSAPP ENVIADO] Para: " + numeroLimpo);
        } catch (Exception e) {
            System.err.println("❌ [ERRO WHATSAPP] Falha ao enviar para " + numeroLimpo + ": " + e.getMessage());
        }
    }
}