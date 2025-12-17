package com.mbarca.vete.dto.request;

import lombok.Data;

@Data
public class CheckoutCustomerRequestDto {
    private String name;
    private String email;
    private String phone;
    private String notes;
}

