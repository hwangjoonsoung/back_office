package org.cric.back_office.global.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}") // Access Token 만료 시간
    private Long expiration;

    @Value("${jwt.refresh-expiration}") // Refresh Token 만료 시간
    private Long refreshExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Access Token 생성 (tokenId, role 포함)
     */
    public String generateToken(Long userId, String email, String name, String tokenId, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .claim("name", name)
                .claim("tokenId", tokenId)
                .claim("role", role)
                .claim("tokenType", "ACCESS")
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 새로운 토큰 ID 생성
     */
    public String generateTokenId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Access Token 만료 시간 반환 (밀리초)
     */
    public Long getAccessTokenExpiration() {
        return expiration;
    }

    /**
     * Refresh Token 생성
     */
    public String generateRefreshToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshExpiration);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("tokenType", "REFRESH")
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Refresh Token 만료 시간 반환 (밀리초)
     */
    public Long getRefreshExpiration() {
        return refreshExpiration;
    }

    public String getNameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("name", String.class);
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return Long.parseLong(claims.getSubject());
    }

    public String getEmailFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("email", String.class);
    }

    public String getTokenIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("tokenId", String.class);
    }

    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getRoleFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("role", String.class);
    }
}
