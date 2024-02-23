package com.axis.authbackend.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@NoArgsConstructor
public class JwtUtility {
    @Value("${jwt.access.token.secret}")
    private String accessTokenSecret;

    @Value("${jwt.access.token.expiry}")
    private long accessTokenExpiry;

    @Value("${jwt.refresh.token.secret}")
    private String refreshTokenSecret;

    @Value("${jwt.refresh.token.expiry}")
    private long refreshTokenExpiry;

    private final SecretKey accessKey = Keys.hmacShaKeyFor(accessTokenSecret.getBytes(StandardCharsets.UTF_8));
    private final SecretKey refreshKey = Keys.hmacShaKeyFor(refreshTokenSecret.getBytes(StandardCharsets.UTF_8));

    public String generateAccessToken(String name, String email) {
        return Jwts.builder()
                .setSubject(name)
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiry))
                .signWith(accessKey)
                .compact();
    }

    public String generateRefreshToken(Long id) {
        return Jwts.builder()
                .setSubject(id.toString())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiry))
                .signWith(refreshKey)
                .compact();
    }

    public String extractUsernameFromRefreshToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(refreshTokenSecret)
                .build()
                .parseClaimsJwt(token)
                .getBody()
                .getSubject();
    }

    public String extractUsernameFromAccessToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(accessTokenSecret)
                .build()
                .parseClaimsJwt(token)
                .getBody()
                .getSubject();
    }
}
