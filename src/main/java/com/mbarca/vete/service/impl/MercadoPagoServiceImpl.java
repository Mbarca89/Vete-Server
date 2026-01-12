package com.mbarca.vete.service.impl;

import com.mbarca.vete.domain.WebOrder;
import com.mbarca.vete.domain.WebOrderItem;
import com.mbarca.vete.dto.request.CheckoutItemRequestDto;
import com.mbarca.vete.dto.request.CheckoutRequestDto;
import com.mbarca.vete.repository.ProductRepository;
import com.mbarca.vete.repository.WebOrderItemRepository;
import com.mbarca.vete.repository.WebOrderRepository;
import com.mbarca.vete.service.MercadoPagoService;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MercadoPagoServiceImpl implements MercadoPagoService{

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @Value("${frontend.base-url}")
    private String frontendBaseUrl;

    @Value("${mercadopago.webhook-url}")
    private String webhookUrl;

    private final WebOrderRepository webOrderRepository;
    private final WebOrderItemRepository webOrderItemRepository;
    private final ProductRepository productRepository;

    @Transactional
    public String createPreference(CheckoutRequestDto request) {

        // 1️⃣ validar stock + calcular total
        BigDecimal total = BigDecimal.ZERO;

        for (CheckoutItemRequestDto item : request.getItems()) {
            Integer stock = productRepository.getStock(item.getId());
            if (stock < item.getQuantity()) {
                throw new RuntimeException(
                        "Stock insuficiente para el producto: " + item.getTitle()
                );
            }
            total = total.add(
                    item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
            );
        }

        // 2️⃣ crear orden PENDING
        WebOrder order = new WebOrder();
        order.setCustomerName(request.getCustomer().getName());
        order.setCustomerEmail(request.getCustomer().getEmail());
        order.setCustomerPhone(request.getCustomer().getPhone());
        order.setTotalAmount(total);
        order.setStatus("PENDING");

        Long orderId = webOrderRepository.create(order);

        // 3️⃣ items + descuento stock
        for (CheckoutItemRequestDto item : request.getItems()) {

            WebOrderItem orderItem = new WebOrderItem();
            orderItem.setWebOrderId(orderId);
            orderItem.setProductId(item.getId());
            orderItem.setProductName(item.getTitle());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setUnitPrice(item.getUnitPrice());

            webOrderItemRepository.create(orderItem);

            Integer currentStock = productRepository.getStock(item.getId());
            productRepository.updateStock(
                    item.getId(),
                    currentStock - item.getQuantity()
            );
        }


        // 4️⃣ preference MP
        List<PreferenceItemRequest> mpItems = request.getItems().stream()
                .map(i -> PreferenceItemRequest.builder()
                        .id(i.getId().toString())
                        .title(i.getTitle())
                        .quantity(i.getQuantity())
                        .unitPrice(i.getUnitPrice())
                        .currencyId("ARS")
                        .build())
                .toList();

        PreferenceRequest pref = PreferenceRequest.builder()
                .items(mpItems)
                .externalReference(orderId.toString()) // 👈 CLAVE
                .payer(PreferencePayerRequest.builder()
                        .email(request.getCustomer().getEmail())
                        .build())
                .backUrls(PreferenceBackUrlsRequest.builder()
                        .success(frontendBaseUrl + "/checkout/success")
                        .failure(frontendBaseUrl + "/checkout/failure")
                        .pending(frontendBaseUrl + "/checkout/pending")
                        .build())
                .notificationUrl(webhookUrl + "/api/v1/mercadopago/webhook")
                .autoReturn("all")
                .build();


        Preference preference;
        try {
            preference = new PreferenceClient().create(pref);
        } catch (MPApiException e) {
            log.error("MPApiException status={} content={}",
                    e.getApiResponse() != null ? e.getApiResponse().getStatusCode() : null,
                    e.getApiResponse() != null ? e.getApiResponse().getContent() : null,
                    e
            );
            throw new RuntimeException(
                    "Error API Mercado Pago: " + e.getApiResponse().getContent(),
                    e
            );
        } catch (MPException e) {
            // Error interno del SDK / config
            throw new RuntimeException(
                    "Error SDK Mercado Pago",
                    e
            );
        }

        // 5️⃣ guardar preferenceId
        webOrderRepository.updatePreference(orderId, preference.getId());

        return preference.getInitPoint();
    }
}

