package com.mbarca.vete.dto.response;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AuthResponseDto {
    Long id;
    String name;
    String surname;
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
}
