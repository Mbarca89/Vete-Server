package com.mbarca.vete.dto.request;

import com.mbarca.vete.domain.OrderProduct;
import com.mbarca.vete.domain.SaleProduct;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrderRequestDto {
    private BigDecimal amount;
    private Date date;
    private List<OrderProduct> orderProducts;

    public OrderRequestDto() {
    }

    public OrderRequestDto(BigDecimal amount, Date date, List<OrderProduct> orderProducts) {
        this.amount = amount;
        this.date = date;
        this.orderProducts = orderProducts;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }
}
