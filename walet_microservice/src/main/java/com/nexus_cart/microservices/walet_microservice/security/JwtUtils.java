package com.nexus_cart.microservices.walet_microservice.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.nexus_cart.microservices.walet_microservice.users.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Utility component responsible for generating, parsing, and validating JSON Web Tokens (JWT).
 * <p>
 * Handles claims generation (including user identity, email, and security role) 
 * to support cross-service authentication and Role-Based Access Control (RBAC).
 */
@Component
public class JwtUtils {

    /** Secret key used for signing JWT tokens. */
    @Value("${jwt.secret:NexusCartSuperSecretKeyForWalletMicroservice2026!}")
    private String jwtSecret;

    /** Token validity duration in milliseconds. Defaults to 24 hours (86,400,000 ms). */
    @Value("${jwt.expirationMs:86400000}")
    private long jwtExpirationMs;

    /**
     * Constructs a cryptographic {@link SecretKey} from the configured secret string.
     *
     * @return HMAC SHA-256 key object.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a signed JWT token containing user identity claims and role authority.
     *
     * @param userId Unique identifier of the authenticated user.
     * @param email User's registered email address.
     * @param role Assigned security role (e.g., ROLE_USER, ROLE_ADMIN).
     * @return Signed JWT compact string.
     */
    public String generateToken(String userId, String email, Role role) {
        return Jwts.builder()
                .subject(userId)
                .claim("email", email)
                .claim("role", role.name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extracts the user ID (subject claim) from a verified JWT token.
     *
     * @param token Compact JWT string.
     * @return The user ID string stored in the token subject.
     * @throws JwtException If the token signature is invalid or corrupted.
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
     * @throws JwtException If the token payload cannot be parsed.
     */
    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String role = claims.get("role", String.class);
        String authority = (role != null) ? role : Role.ROLE_USER.name();

        return List.of(new SimpleGrantedAuthority(authority));
    }

    /**
     * Validates the integrity, signature, and expiration status of a JWT token.
     *
     * @param token Compact JWT string to validate.
     * @return {@code true} if token is valid and active; {@code false} if expired, tampered, or malformed.
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