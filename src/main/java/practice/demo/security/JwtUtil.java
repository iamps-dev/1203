package practice.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import practice.demo.entity.User;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET =
            "THIS_IS_A_VERY_SECURE_SECRET_KEY_FOR_JWT_AUTH_256_BITS_LONG";

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    private final long USER_EXPIRATION = 1000 * 60 * 60 * 24; // 24 hours
    private final long ADMIN_EXPIRATION = 1000 * 60 * 60;     // 1 hour

    // ================= USER TOKEN =================
    public String generateUserToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", "USER")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + USER_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ================= ADMIN / SUPER_ADMIN TOKEN =================
    public String generateAdminToken(User admin) {
        return Jwts.builder()
                .setSubject(admin.getEmail())
                .claim("role", admin.getRole())
                .claim("tokenVersion", admin.getPasswordVersion())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ADMIN_EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // ================= COMMON METHODS =================
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public Integer extractTokenVersion(String token) {
        return extractAllClaims(token).get("tokenVersion", Integer.class);
    }

    // ✅ ADD THIS METHOD (THIS FIXES THE ERROR)
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }
}
