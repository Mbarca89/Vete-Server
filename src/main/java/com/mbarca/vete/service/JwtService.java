package com.mbarca.vete.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.function.Function;

public interface JwtService {
    public String getToken(UserDetails user);

    boolean isTokenValid(String token, UserDetails userDetails);

    public String getUserNameFromToken(String token);

    public Claims getAllClaims(String token);
    public <T> T getClaim(String token, Function<Claims, T> claimResolver);
    public Date getExpiration(String token);
    public boolean isTokenExpired (String token);
}
