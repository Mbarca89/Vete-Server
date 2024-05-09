package com.mbarca.vete.dto.response;

import com.mbarca.vete.domain.OrderProduct;
import com.mbarca.vete.domain.SaleProduct;

import java.math.BigDecimal;
import java.util.List;

public class OrderResponseDto {
    private Long id;
    private BigDecimal amount;
    private String date;
    private List<OrderProduct> orderProducts;

    public OrderResponseDto() {
    }

    public OrderResponseDto(Long id, BigDecimal amount, String date, List<OrderProduct> orderProducts) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.orderProducts = orderProducts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }
}
