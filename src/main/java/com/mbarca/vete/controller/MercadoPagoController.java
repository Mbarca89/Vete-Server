package com.mbarca.vete.controller;

import com.mbarca.vete.dto.request.CheckoutRequestDto;
import com.mbarca.vete.dto.response.CheckoutResponseDto;
import com.mbarca.vete.service.MercadoPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mercadopago/public")
public class MercadoPagoController {

    private final MercadoPagoService mercadoPagoService;

    public MercadoPagoController(MercadoPagoService mercadoPagoService) {
        this.mercadoPagoService = mercadoPagoService;
    }

    @PostMapping("/create-checkout")
    public ResponseEntity<CheckoutResponseDto> createCheckout(
            @RequestBody CheckoutRequestDto request
    ) {
        String initPoint = mercadoPagoService.createPreference(request);
        return ResponseEntity.ok(new CheckoutResponseDto(initPoint));
    }
}

