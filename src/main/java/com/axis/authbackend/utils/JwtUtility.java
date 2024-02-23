package com.axis.authbackend.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@NoArgsConstructor
public class JwtUtility {
    private static final long ACCESS_TOKEN_EXPIRY = 3_600_000;
    private static final String ACCESS_TOKEN_SECRET= "Ysn6JkhVuzMXBBxGPJq7n7OoP3Yz6SvKMP1Yog+Jg5Q=";

    private static final long REFRESH_TOKEN_EXPIRY = 1_209_600_000;
    private static final String REFRESH_TOKEN_SECRET="Ysn6JkhVuzMXBBxGPJq7n7OoP3Yz6SvKMP1Yog+Jg5Q=";

    private static final SecretKey accessKey = Keys.hmacShaKeyFor(ACCESS_TOKEN_SECRET.getBytes(StandardCharsets.UTF_8));
    private static final SecretKey refreshKey = Keys.hmacShaKeyFor(REFRESH_TOKEN_SECRET.getBytes(StandardCharsets.UTF_8));


    public static String generateAccessToken(String name, String email) {
        return Jwts.builder()
                .setSubject(name)
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY))
                .signWith(accessKey)
                .compact();
    }

    public static String generateRefreshToken(Long id) {
        return Jwts.builder()
                .setSubject(id.toString())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRY))
                .signWith(refreshKey)
                .compact();
    }

    public static String extractUsernameFromRefreshToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(REFRESH_TOKEN_SECRET)
                .build()
                .parseClaimsJwt(token)
                .getBody()
                .getSubject();
    }

    public static String extractUsernameFromAccessToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(ACCESS_TOKEN_SECRET)
                .build()
                .parseClaimsJwt(token)
                .getBody()
                .getSubject();
    }
}
