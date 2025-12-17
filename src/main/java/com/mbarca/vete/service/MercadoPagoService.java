package com.mbarca.vete.service;

import com.mbarca.vete.dto.request.CheckoutRequestDto;

public interface MercadoPagoService {
    String createPreference(CheckoutRequestDto request);
}
