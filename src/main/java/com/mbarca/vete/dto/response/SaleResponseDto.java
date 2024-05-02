package com.mbarca.vete.dto.response;

import com.mbarca.vete.domain.SaleProduct;

import java.math.BigDecimal;
import java.util.List;

public class SaleResponseDto {
    private Long id;
    private BigDecimal amount;
    private BigDecimal cost;
    private String date;
    private String seller;
    private List<SaleProduct> saleProducts;

    public SaleResponseDto() {
    }

    public SaleResponseDto(Long id, BigDecimal amount, BigDecimal cost, String date, String seller, List<SaleProduct> saleProducts) {
        this.id = id;
        this.amount = amount;
        this.cost = cost;
        this.date = date;
        this.seller = seller;
        this.saleProducts = saleProducts;
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

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public List<SaleProduct> getSaleProducts() {
        return saleProducts;
    }

    public void setSaleProducts(List<SaleProduct> saleProducts) {
        this.saleProducts = saleProducts;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
