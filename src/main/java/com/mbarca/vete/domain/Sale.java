package com.mbarca.vete.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sale {
    private Long id;
    private BigDecimal amount;
    private BigDecimal cost;
    private Date date;
    private String seller;
    private List<SaleProduct> saleProducts;
    public Sale() {
    }

    public Sale(Long id, BigDecimal amount, BigDecimal cost, Date date, String seller, List<SaleProduct> saleProducts) {
        this.id = id;
        this.amount = amount;
        this.cost = cost;
        this.date = date;
        this.seller = seller;
        this.saleProducts = saleProducts;
    }

    public void addSaleProduct(SaleProduct saleProduct) {
        if (saleProducts == null) {
            saleProducts = new ArrayList<>();
        }
        saleProducts.add(saleProduct);
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

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
