package com.mbarca.vete.domain;
import javax.persistence.Lob;
import java.util.Arrays;

public class Product {
    private Long id;
    private String name;
    private String description;
    private Double barCode;
    private Double cost;
    private Double price;
    private Integer stock;
    private String categoryName;
    private String providerName;
    private Boolean stockAlert;
    private Boolean published;
    @Lob
    private byte[] image;
    @Lob
    private byte[] thumbnail;

    public Product() {
    }

    public Product(Long id, String name, String description, Double barCode, Double cost, Double price, Integer stock, String categoryName, String providerName, Boolean stockAlert, Boolean published, byte[] image, byte[] thumbnail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.barCode = barCode;
        this.cost = cost;
        this.price = price;
        this.stock = stock;
        this.categoryName = categoryName;
        this.providerName = providerName;
        this.stockAlert = stockAlert;
        this.published = published;
        this.image = image;
        this.thumbnail = thumbnail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Boolean getStockAlert() {
        return stockAlert;
    }

    public void setStockAlert(Boolean stockAlert) {
        this.stockAlert = stockAlert;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }
    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", barCode=" + barCode +
                ", cost=" + cost +
                ", price=" + price +
                ", stock=" + stock +
                ", categoryName='" + categoryName + '\'' +
                ", provider='" + providerName + '\'' +
                ", photo=" + Arrays.toString(image) +
                ", thumbnail=" + Arrays.toString(thumbnail) +
                '}';
    }

}
