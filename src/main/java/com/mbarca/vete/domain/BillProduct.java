package com.mbarca.vete.domain;

public class BillProduct {
    Long id;
    Long barCode;
    String description;
    Integer quantity;
    Double price;
    Double netPrice;
    Double iva;

    public BillProduct(Long id, Long barCode, String description, Integer quantity, Double price, Double netPrice, Double iva) {
        this.id = id;
        this.barCode = barCode;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.netPrice = netPrice;
        this.iva = iva;
    }

    public BillProduct() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBarCode() {
        return barCode;
    }

    public void setBarCode(Long barCode) {
        this.barCode = barCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(Double netPrice) {
        this.netPrice = netPrice;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }
}
