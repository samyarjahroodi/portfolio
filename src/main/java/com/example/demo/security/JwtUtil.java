package com.example.demo.security;

import com.example.demo.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public String generateToken(String username, Role role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

/*    public Role extractRole(String token) {
        Claims claims = getClaims(token);
        String roleString = claims.get("role", String.class);

        try {
            return Role.valueOf("ROLE_" + roleString);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role found in JWT token: " + roleString);
        }
    }*/

/*    public Role extractRole(String token) {
        String roleString = getClaims(token).get("role", String.class);
        return Role.valueOf(roleString);
    }*/


    public boolean validateToken(String token, String username) {
        return (extractUsername(token)).equals(username) && !isTokenExpired(token);
    }

    Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }
}
