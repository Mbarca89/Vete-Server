package com.mbarca.vete.dto.response;

public class ProviderResponseDto {
    private String name;
    private String contactName;
    private String phone;

    public ProviderResponseDto() {
    }

    public ProviderResponseDto(String name, String contactName, String phone) {
        this.name = name;
        this.contactName = contactName;
        this.phone = phone;
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
        return "ProviderResponseDto{" +
                "name='" + name + '\'' +
                ", contactName='" + contactName + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
