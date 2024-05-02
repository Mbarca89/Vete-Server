package com.mbarca.vete.dto.response;

public class ProviderResponseDto {
    private Long id;
    private String name;
    private String contactName;
    private String phone;

    public ProviderResponseDto() {
    }

    public ProviderResponseDto(String name, String contactName, String phone, Long id) {
        this.id = id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
