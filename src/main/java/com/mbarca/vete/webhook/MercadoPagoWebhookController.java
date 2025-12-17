package com.mbarca.vete.webhook;

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

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String type,
            @RequestBody(required = false) String body
    ) {
        // MP puede mandar id por query o en body, nosotros priorizamos query
        if (id != null && "payment".equals(type)) {
            webhookService.processPayment(id);
        }
        return ResponseEntity.ok().build();
    }
}


