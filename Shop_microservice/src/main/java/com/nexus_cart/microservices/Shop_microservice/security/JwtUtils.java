package com.nexus_cart.microservices.Shop_microservice.security;

import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Utility component for parsing, validating, and extracting claims from JWTs in the Shop Microservice.
 */
@Component
public class JwtUtils {

    @Value("${jwt.secret:NexusCartSuperSecretKeyForWalletMicroservice2026!}")
    private String jwtSecret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Extracts the user ID (subject claim) from a verified JWT token.
     */
    public String getUserIdFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Extracts security authorities derived from the role claim within a JWT token.
     *
     * @param token Compact JWT string.
     * @return List containing the user's {@link GrantedAuthority}.
     */
    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String role = claims.get("role", String.class);
        String authority = (role != null) ? role : "ROLE_USER";

        return List.of(new SimpleGrantedAuthority(authority));
    }

    /**
     * Validates the integrity and expiration status of a JWT token.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}