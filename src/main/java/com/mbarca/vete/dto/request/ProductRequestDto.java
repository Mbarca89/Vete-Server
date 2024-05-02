package com.mbarca.vete.dto.request;

public class ProductRequestDto {
    private Long id;
    private String name;
    private String description;
    private Double barCode;
    private Double cost;
    private Double price;
    private Integer stock;
    private String categoryName;
    private String providerName;

    public ProductRequestDto() {
    }

    public ProductRequestDto(Long id, String name, String description, Double barCode, Double cost, Double price, Integer stock, String categoryName, String providerName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.barCode = barCode;
        this.cost = cost;
        this.price = price;
        this.stock = stock;
        this.categoryName = categoryName;
        this.providerName = providerName;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
                ", category='" + categoryName + '\'' +
                ", provider='" + providerName + '\'' +
                '}';
    }

}
