package com.mbarca.vete.dto.request;

import com.mbarca.vete.domain.SaleProduct;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class SaleRequestDto {
    private BigDecimal amount;
    private BigDecimal cost;
    private Date date;
    private String seller;
    private boolean discount;
    private Double discountAmount;
    private List<SaleProduct> saleProducts;

    public SaleRequestDto() {
    }

    public SaleRequestDto(BigDecimal amount, BigDecimal cost, Date date, String seller, List<SaleProduct> saleProducts) {
        this.amount = amount;
        this.cost = cost;
        this.date = date;
        this.seller = seller;
        this.saleProducts = saleProducts;
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

    @Override
    public String toString() {
        return "SaleRequestDto{" +
                "amount=" + amount +
                ", date=" + date +
                ", sellerId=" + seller +
                ", saleProducts=" + saleProducts +
                '}';
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public boolean isDiscount() {
        return discount;
    }

    public void setDiscount(boolean discount) {
        this.discount = discount;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }
}
