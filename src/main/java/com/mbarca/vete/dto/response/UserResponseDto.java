package com.mbarca.vete.dto.response;

public class UserResponseDto {
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

}
