package com.mbarca.vete.service.impl;

import com.mbarca.vete.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {

    private static final String SECRET_KEY="sUdI3HpxjCXjtPrQngVsl6ZAPLQIQp6iSBiXo2arARM/gt67tz98NhHlX5mCZMG+";

    @Override
    public String getToken(UserDetails user) {
        return getToken(new HashMap<>(), user);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = getUserNameFromToken(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    @Override
    public String getUserNameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    @Override
    public Claims getAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
    }

    @Override
    public <T> T getClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = getAllClaims(token);
        return claimResolver.apply(claims);
    }

    @Override
    public Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    @Override
    public boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    private String getToken(Map<String,Object> extraClaims, UserDetails user) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+(30L * 24 * 60 * 60 * 1000)))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
