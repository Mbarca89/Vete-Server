package com.mbarca.vete.service;

public interface MercadoPagoWebhookService {
    void processPayment(String paymentId);
}
