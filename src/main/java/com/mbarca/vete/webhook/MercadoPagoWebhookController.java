package com.mbarca.vete.webhook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbarca.vete.repository.WebOrderRepository;
import com.mbarca.vete.service.MercadoPagoWebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mercadopago")
@RequiredArgsConstructor
public class MercadoPagoWebhookController {

    private final MercadoPagoWebhookService webhookService;
    private final ObjectMapper objectMapper;


    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String type,
            @RequestBody(required = false) String body
    ) {
        System.out.println("Ingreso a webhook");
        System.out.println("ID: " + id);
        System.out.println("TYPE: " + type);
        System.out.println("BODY: " + body);

        String paymentId = null;
        boolean isPaymentEvent = false;

        // Caso 1: query param type=payment (a veces viene)
        if ("payment".equals(type)) {
            isPaymentEvent = true;
            if (id != null && !id.isBlank()) paymentId = id;
        }

        // Parse body si existe
        if (body != null && !body.isBlank()) {
            try {
                JsonNode root = objectMapper.readTree(body);

                // Caso 2: { type:"payment", data:{id:"..."} }
                String bodyType = root.path("type").asText(null);
                if ("payment".equals(bodyType)) {
                    isPaymentEvent = true;
                    String dataId = root.path("data").path("id").asText(null);
                    if (paymentId == null && dataId != null && !dataId.isBlank()) paymentId = dataId;
                }

                // Caso 3: { topic:"payment", resource:"141..." }
                String topic = root.path("topic").asText(null);
                if ("payment".equals(topic)) {
                    isPaymentEvent = true;
                    String resource = root.path("resource").asText(null);
                    if (paymentId == null && resource != null && resource.matches("\\d+")) paymentId = resource;
                }

                // Si es merchant_order, NO lo procesamos acá
            } catch (Exception e) {
                System.out.println("No se pudo parsear body JSON: " + e.getMessage());
            }
        }

        if (isPaymentEvent && paymentId != null) {
            webhookService.processPayment(paymentId);
        } else {
            System.out.println("Webhook ignorado (no es payment o no trae paymentId).");
        }

        return ResponseEntity.ok().build();
    }
}



