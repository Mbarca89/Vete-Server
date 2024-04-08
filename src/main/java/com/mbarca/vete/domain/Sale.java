package com.mbarca.vete.domain;

public class Sale {
    private Long id;
    private double amount;
    private String date;
    private Long clientId;
    private Long sellerId;

    public Sale() {
    }

    public Sale(Long id, double amount, String date, Long clientId, Long sellerId) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.clientId = clientId;
        this.sellerId = sellerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", amount=" + amount +
                ", date='" + date + '\'' +
                ", clientId=" + clientId +
                ", sellerId=" + sellerId +
                '}';
    }
}
