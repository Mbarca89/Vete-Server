package com.mbarca.vete.dto.response;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AuthResponseDto {
    String token;
    String userName;
    Collection role;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Collection getRole() {
        return role;
    }

    public void setRole(Collection role) {
        this.role = role;
    }
}
