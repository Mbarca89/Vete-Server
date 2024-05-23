package com.mbarca.vete.domain;

public class MonthlyReport {
    private double totalAmount;
    private double totalCost;
    private double payments;
    public MonthlyReport() {
    }

    public MonthlyReport(double totalAmount, double totalCost, double payments) {
        this.totalAmount = totalAmount;
        this.totalCost = totalCost;
        this.payments = payments;
    }

    public MonthlyReport(double totalAmount, double totalCost) {
        this.totalAmount = totalAmount;
        this.totalCost = totalCost;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
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
}
