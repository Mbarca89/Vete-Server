package com.mbarca.vete.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class WebOrderResponseDto {

    private Long id;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime createdAt;
    private boolean shipped;
}
