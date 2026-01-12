package com.mbarca.vete.dto.response;

import java.math.BigDecimal;

public record WebOrderStatusResponse(
        Long orderId,
        String status,
        String paymentId,
        String preferenceId,
        BigDecimal totalAmount
) {}
