package com.mbarca.vete.dto.response;

public class CategoryTotalResponseDto {

    private String categoryName;
    private double totalAmount;

    public CategoryTotalResponseDto(String categoryName, double totalAmount) {
        this.categoryName = categoryName;
        this.totalAmount = totalAmount;
    }

    public CategoryTotalResponseDto() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
