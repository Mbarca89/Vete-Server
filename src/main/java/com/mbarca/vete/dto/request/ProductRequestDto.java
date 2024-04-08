package com.mbarca.vete.dto.request;

public class ProductRequestDto {
    private String name;
    private String description;
    private Double barCode;
    private Double cost;
    private Double price;
    private Integer stock;
    private String category;
    private String provider;

    public ProductRequestDto() {
    }

    public ProductRequestDto(String name, String description, Double barCode, Double cost, Double price, Integer stock, String category, String provider) {
        this.name = name;
        this.description = description;
        this.barCode = barCode;
        this.cost = cost;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.provider = provider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getBarCode() {
        return barCode;
    }

    public void setBarCode(Double barCode) {
        this.barCode = barCode;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public ProductRequestDto(String name, String description, Double barCode, Double cost, Double price, Integer stock, String category, String seller, String provider) {
        this.name = name;
        this.description = description;
        this.barCode = barCode;
        this.cost = cost;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.provider = provider;
    }

    @Override
    public String toString() {
        return "ProductRequestDto{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", barCode=" + barCode +
                ", cost=" + cost +
                ", price=" + price +
                ", stock=" + stock +
                ", category='" + category + '\'' +
                ", provider='" + provider + '\'' +
                '}';
    }
}
