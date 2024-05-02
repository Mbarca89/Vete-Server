package com.mbarca.vete.dto.request;

public class ProviderRequestDto {
    private Long id;
    private String name;
    private String contactName;
    private String phone;

    public ProviderRequestDto() {
    }

    public ProviderRequestDto(String name, String contactName, String phone, Long id) {
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
        return "ProviderRequestDto{" +
                "name='" + name + '\'' +
                ", contactName='" + contactName + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

}
