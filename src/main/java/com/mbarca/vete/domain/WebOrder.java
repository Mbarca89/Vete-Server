package com.mbarca.vete.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WebOrder {

    private Long id;

    private String customerName;
    private String customerEmail;
    private String customerPhone;

    private BigDecimal totalAmount;
    private String status; // PENDING, APPROVED, REJECTED

    private String preferenceId;
    private String paymentId;

    private LocalDateTime createdAt;
}
