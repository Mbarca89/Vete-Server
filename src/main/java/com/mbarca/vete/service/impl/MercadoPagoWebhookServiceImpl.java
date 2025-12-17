package com.mbarca.vete.service.impl;

import com.mbarca.vete.domain.WebOrder;
import com.mbarca.vete.domain.WebOrderItem;
import com.mbarca.vete.repository.ProductRepository;
import com.mbarca.vete.repository.WebOrderItemRepository;
import com.mbarca.vete.repository.WebOrderRepository;
import com.mbarca.vete.service.MercadoPagoWebhookService;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.resources.payment.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MercadoPagoWebhookServiceImpl implements MercadoPagoWebhookService {

    private final WebOrderRepository webOrderRepository;
    private final WebOrderItemRepository webOrderItemRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void processPayment(String paymentId) {

        try {
            Payment payment = new PaymentClient().get(Long.parseLong(paymentId));

            String status = payment.getStatus();          // approved, rejected, pending
            String externalReference = payment.getExternalReference();

            if (externalReference == null) {
                log.warn("Payment {} sin external_reference", paymentId);
                return;
            }

            Long orderId = Long.parseLong(externalReference);

            WebOrder order = webOrderRepository.findById(orderId);

            if ("approved".equals(status)) {
                webOrderRepository.updatePayment(
                        order.getId(),
                        paymentId,
                        "APPROVED"
                );
                log.info("Pago aprobado - Order {}", order.getId());
                return;
            }

            if ("rejected".equals(status) || "cancelled".equals(status)) {

                // devolver stock
                List<WebOrderItem> items = webOrderItemRepository.findByOrderId(order.getId());
                for (WebOrderItem item : items) {
                    productRepository.increaseStock(
                            item.getProductId(),
                            item.getQuantity()
                    );
                }

                webOrderRepository.updatePayment(
                        order.getId(),
                        paymentId,
                        "REJECTED"
                );

                log.warn("Pago rechazado - Order {}", order.getId());
            }

        } catch (Exception e) {
            log.error("Error procesando webhook de Mercado Pago", e);
        }
    }
}
