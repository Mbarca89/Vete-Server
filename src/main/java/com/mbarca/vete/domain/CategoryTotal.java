package com.mbarca.vete.domain;

public class CategoryTotal {

    private String categoryName;
    private double totalAmount;

    public CategoryTotal(String categoryName, double totalAmount) {
        this.categoryName = categoryName;
        this.totalAmount = totalAmount;
    }

    public CategoryTotal() {
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
