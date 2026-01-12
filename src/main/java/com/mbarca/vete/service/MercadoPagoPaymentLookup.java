package com.mbarca.vete.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class MercadoPagoPaymentLookup {

    @Value("${mercadopago.access-token}")
    private String accessToken;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Optional<PaymentInfo> findLatestByExternalReference(String externalReference) {
        try {
            String url = "https://api.mercadopago.com/v1/payments/search?sort=date_created&criteria=desc&limit=1&external_reference="
                    + URLEncoder.encode(externalReference, StandardCharsets.UTF_8);

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> resp;
            try (HttpClient client = HttpClient.newHttpClient()) {
                resp = client.send(req, HttpResponse.BodyHandlers.ofString());
            }

            if (resp.statusCode() >= 400) {
                // Ojo: 401 acá es típico si estás mezclando credenciales test/prod
                return Optional.empty();
            }

            JsonNode root = objectMapper.readTree(resp.body());
            JsonNode results = root.get("results");
            if (results == null || !results.isArray() || results.isEmpty()) {
                return Optional.empty();
            }

            JsonNode p = results.get(0);
            String id = p.path("id").asText(null);
            String status = p.path("status").asText(null); // approved, pending, rejected, cancelled...
            return Optional.of(new PaymentInfo(id, status));

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public record PaymentInfo(String paymentId, String status) {}
}

