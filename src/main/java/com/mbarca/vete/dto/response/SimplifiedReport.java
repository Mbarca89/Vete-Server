package com.mbarca.vete.dto.response;

public class SimplifiedReport {
    private Double totalSaleAmount;
    private Double totalSaleCost;
    private Double totalOrderAmount;
    private Double totalBillAmount;
    private Double totalPaymentAmount;

    public SimplifiedReport(Double totalSaleAmount, Double totalSaleCost, Double totalOrderAmount, Double totalBillAmount, Double totalPaymentAmount) {
        this.totalSaleAmount = totalSaleAmount;
        this.totalSaleCost = totalSaleCost;
        this.totalOrderAmount = totalOrderAmount;
        this.totalBillAmount = totalBillAmount;
        this.totalPaymentAmount = totalPaymentAmount;
    }


    // Getters y Setters
    public Double getTotalSaleAmount() {
        return totalSaleAmount;
    }

    public void setTotalSaleAmount(Double totalSaleAmount) {
        this.totalSaleAmount = totalSaleAmount;
    }

    public Double getTotalSaleCost() {
        return totalSaleCost;
    }

    public void setTotalSaleCost(Double totalSaleCost) {
        this.totalSaleCost = totalSaleCost;
    }

    public Double getTotalOrderAmount() {
        return totalOrderAmount;
    }

    public void setTotalOrderAmount(Double totalOrderAmount) {
        this.totalOrderAmount = totalOrderAmount;
    }

    public Double getTotalBillAmount() {
        return totalBillAmount;
    }

    public void setTotalBillAmount(Double totalBillAmount) {
        this.totalBillAmount = totalBillAmount;
    }

    public Double getTotalPaymentAmount() {
        return totalPaymentAmount;
    }

    public void setTotalPaymentAmount(Double totalPaymentAmount) {
        this.totalPaymentAmount = totalPaymentAmount;
    }
}
