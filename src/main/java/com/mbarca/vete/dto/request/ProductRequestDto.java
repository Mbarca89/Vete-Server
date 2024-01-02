package com.mbarca.vete.dto.request;

public class ProductRequestDto {
    private String name;
    private Double cost;
    private Double price;
    private Integer stock;
    private String categoryName;
    private String seller;
    private String provider;

    public ProductRequestDto() {
    }

    public ProductRequestDto(String name, Double cost, Double price, Integer stock, String categoryName, String seller, String provider) {
        this.name = name;
        this.cost = cost;
        this.price = price;
        this.stock = stock;
        this.categoryName = categoryName;
        this.seller = seller;
        this.provider = provider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
    @Override
    public String toString() {
        return "ProductRequestDto{" +
                "name='" + name + '\'' +
                ", cost=" + cost +
                ", price=" + price +
                ", stock=" + stock +
                ", categoryName='" + categoryName + '\'' +
                ", seller='" + seller + '\'' +
                '}';
    }

}
