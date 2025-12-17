package com.mbarca.vete.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WebOrderItem {

    private Long id;

    private Long webOrderId;
    private Long productId;
    private String productName;

    private Integer quantity;
    private BigDecimal unitPrice;
}
