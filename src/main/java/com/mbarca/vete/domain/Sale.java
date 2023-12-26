package com.mbarca.vete.domain;

public class Sale {
    private Long id;
    private String name;
    private String date;
    private Long clientId;

    public Sale() {
    }

    public Sale(String name, String date, Long clientId) {
        this.name = name;
        this.date = date;
        this.clientId = clientId;
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

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", clientId=" + clientId +
                '}';
    }
}
