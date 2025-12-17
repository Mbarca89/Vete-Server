package com.mbarca.vete.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CheckoutRequestDto {
    private CheckoutCustomerRequestDto customer;
    private List<CheckoutItemRequestDto> items;
}
