package com.mbarca.vete.domain;

public class Provider {
    private Long id;
    private String name;
    private String contactName;
    private String phone;

    public Provider() {
    }

    public Provider(String name, String contactName, String phone) {
        this.name = name;
        this.contactName = contactName;
        this.phone = phone;
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

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Provider{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contactName='" + contactName + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
