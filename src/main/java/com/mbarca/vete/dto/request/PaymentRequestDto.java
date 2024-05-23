package com.mbarca.vete.dto.request;

public class PaymentRequestDto {
    private Long id;
    private String billNumber;
    private Double amount;
    private String provider;
    private Boolean payed;
    private String paymentMethod;

    public PaymentRequestDto() {
    }

    public PaymentRequestDto(Long id, String billNumber, Double amount, String provider, Boolean payed, String paymentMethod) {
        this.id = id;
        this.billNumber = billNumber;
        this.amount = amount;
        this.provider = provider;
        this.payed = payed;
        this.paymentMethod = paymentMethod;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
