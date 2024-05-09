package com.mbarca.vete.domain;

public class StockAlert {
    private String productName;
    private Integer stock;

    public StockAlert() {
    }

    public StockAlert(String productName, Integer stock) {
        this.productName = productName;
        this.stock = stock;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
