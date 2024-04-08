package com.mbarca.vete.dto.request;

public class ClientRequestDto {
    private Long id;
    private String name;
    private String surname;
    private  String phone;

    public ClientRequestDto() {
    }

    public ClientRequestDto(Long id, String name, String surname, String phone) {
        this.id = id;
        this.name = name;
        this.surname = surname;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "ClientRequestDto{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

}
