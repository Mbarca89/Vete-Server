package com.mbarca.vete.dto.response;

public class ProductResponseDto {
    private Long id;
    private String name;
    private String description;
    private Double barCode;
    private Double cost;
    private Double price;
    private Integer stock;
    private String categoryName;
    private String provider;
    private byte[] image;

    public ProductResponseDto() {
    }

    public ProductResponseDto(String name, Double cost, Double price, Integer stock, String categoryName, String provider) {
        this.name = name;
        this.cost = cost;
        this.price = price;
        this.stock = stock;
        this.categoryName = categoryName;
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

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Override
    public String toString() {
        return "ProductResponseDto{" +
                "name='" + name + '\'' +
                ", cost=" + cost +
                ", price=" + price +
                ", stock=" + stock +
                ", categoryName='" + categoryName + '\'' +
                ", provider='" + provider + '\'' +
                '}';
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
