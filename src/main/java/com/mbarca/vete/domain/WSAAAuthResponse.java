package com.mbarca.vete.domain;

public class WSAAAuthResponse {
    String token;
    String sign;
    String message;

    public WSAAAuthResponse(String token, String sign, String message) {
        this.token = token;
        this.sign = sign;
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
