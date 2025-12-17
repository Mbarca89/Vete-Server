package com.mbarca.vete.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CheckoutItemRequestDto {
    private Long id;
    private String title;
    private Integer quantity;
    private BigDecimal unitPrice;
}
