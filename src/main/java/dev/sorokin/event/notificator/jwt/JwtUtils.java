package dev.sorokin.event.notificator.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtils {
    private static SecretKey secretKey;

    @Value("${jwt.secret-key}")
    private String secretTempKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(secretTempKey.getBytes());
    }

    public static boolean isValidToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token); // check valid and expiration token

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public static Long getUserIdFromToken(String token) {
        Object id = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("id");

        return ((Number) id).longValue();
    }

    public static String getUserRoleFromToken(String token) {
        return (String) Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role");
    }
}
