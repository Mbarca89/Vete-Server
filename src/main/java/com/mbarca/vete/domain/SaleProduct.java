package com.mbarca.vete.domain;

public class SaleProduct {
    private Long saleId;
    private Long productId;
    private int quantity;
    private String productName;
    private String productDescription;
    private String productPrice;
    private String productCost;

    public SaleProduct() {
    }

    public SaleProduct(Long saleId, Long productId, int quantity, String productName, String productDescription, String productPrice, String productCost) {
        this.saleId = saleId;
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productCost = productCost;
    }

    public long getSaleId() {
        return saleId;
    }

    public void setSaleId(long saleId) {
        this.saleId = saleId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductCost() {
        return productCost;
    }

    public void setProductCost(String productCost) {
        this.productCost = productCost;
    }
}
