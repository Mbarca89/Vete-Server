package com.mbarca.vete.domain;

import java.util.Date;

public class Payment {
    private Long id;
    private Date date;
    private String billNumber;
    private Double amount;
    private String provider;
    private Boolean payed;
    private String paymentMethod;
    private Date paymentDate;

    public Payment() {
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", date=" + date +
                ", billNumber='" + billNumber + '\'' +
                ", amount=" + amount +
                ", provider='" + provider + '\'' +
                ", payed=" + payed +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentDate=" + paymentDate +
                '}';
    }

    public Payment(Long id, Date date, String billNumber, Double amount, String provider, Boolean payed, String paymentMethod, Date paymentDate) {
        this.id = id;
        this.date = date;
        this.billNumber = billNumber;
        this.amount = amount;
        this.provider = provider;
        this.payed = payed;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Boolean getPayed() {
        return payed;
    }

    public void setPayed(Boolean payed) {
        this.payed = payed;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
