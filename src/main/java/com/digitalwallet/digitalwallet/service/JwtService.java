
package com.digitalwallet.digitalwallet.service;

import com.digitalwallet.digitalwallet.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                io.jsonwebtoken.io.Decoders.BASE64.decode(secret)
        );
    }

    public String generateToken(User user) {
        long currentTime = System.currentTimeMillis();

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("tokenVersion", user.getTokenVersion())
                .issuedAt(new Date(currentTime))
                .expiration(new Date(currentTime + 15 * 60 * 1000))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(String token, User user) {
        String email = extractEmail(token);
        return email.equals(user.getEmail());
    }

    public Instant extractExpiration(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .toInstant();
    }

    public boolean isTokenVersionValid(String token, User user) {
        Integer tokenVersion = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("tokenVersion", Integer.class);

        return tokenVersion != null
                && tokenVersion.equals(user.getTokenVersion());
    }
}

