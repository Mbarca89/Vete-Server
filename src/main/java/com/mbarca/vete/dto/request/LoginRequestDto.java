package com.mbarca.vete.dto.request;

public class LoginRequestDto {
    String userName;
    String password;

    public LoginRequestDto(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }
    public String getPassword() {
        return password;
    }
}
