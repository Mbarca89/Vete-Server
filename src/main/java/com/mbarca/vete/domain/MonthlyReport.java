package com.mbarca.vete.domain;

public class MonthlyReport {
    private double totalSaleAmount;
    private double totalOrderAmount;
    private double stockCost;
    private double stockPotentialSales;
    private double totalCost;
    private double payments;
    public MonthlyReport() {
    }

    public MonthlyReport(double totalSaleAmount, double totalOrderAmount, double stockCost, double stockPotentialSales, double totalCost, double payments) {
        this.totalSaleAmount = totalSaleAmount;
        this.totalOrderAmount = totalOrderAmount;
        this.stockCost = stockCost;
        this.stockPotentialSales = stockPotentialSales;
        this.totalCost = totalCost;
        this.payments = payments;
    }

    public MonthlyReport(double totalSaleAmount, double totalCost) {
        this.totalSaleAmount = totalSaleAmount;
        this.totalCost = totalCost;
    }

    public double getTotalSaleAmount() {
        return totalSaleAmount;
    }

    public void setTotalSaleAmount(double totalSaleAmount) {
        this.totalSaleAmount = totalSaleAmount;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getPayments() {
        return payments;
    }

    public void setPayments(double payments) {
        this.payments = payments;
    }

    public double getTotalOrderAmount() {
        return totalOrderAmount;
    }

    public void setTotalOrderAmount(double totalOrderAmount) {
        this.totalOrderAmount = totalOrderAmount;
    }

    public double getStockCost() {
        return stockCost;
    }

    public void setStockCost(double stockCost) {
        this.stockCost = stockCost;
    }

    public double getStockPotentialSales() {
        return stockPotentialSales;
    }

    public void setStockPotentialSales(double stockPotentialSales) {
        this.stockPotentialSales = stockPotentialSales;
    }
}
