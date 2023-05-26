package com.mesh.task.autorization;

import com.mesh.task.entity.*;
import io.jsonwebtoken.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class JwtUtil {

    private static final String SECRET = "bearerNonVeryBadButMaySometimesAndTodayWeOpenForAllTimes";
    private static final long EXPIRATION_TIME = 3600000; // 1 hour

    public String generateToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getId().toString());
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiration)
            .signWith(SignatureAlgorithm.HS256, SECRET)
            .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(SECRET)
            .parseClaimsJws(token)
            .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            System.out.println("Invalid JWT signature: " + ex.getMessage());
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token: " + ex.getMessage());
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token: " + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty: " + ex.getMessage());
        }
        return false;
    }
}
