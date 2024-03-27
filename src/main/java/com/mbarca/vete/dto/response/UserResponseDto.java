package com.mbarca.vete.dto.response;

public class UserResponseDto {
    Long id;
    String name;
    String surname;
    String userName;
    String role;

    public UserResponseDto(String userName, String role, String token) {
        this.userName = userName;
        this.role = role;
    }

    public UserResponseDto() {
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
