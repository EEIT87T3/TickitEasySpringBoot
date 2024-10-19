package com.eeit87t3.tickiteasy.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTUtil {

    // 生成一個安全的密鑰，符合 HS256 的要求
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 86400000; // 1 天（毫秒）

    // 生成 JWT
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)  // 使用者的 email 作為 subject
                .setIssuedAt(new Date())  // JWT 發行時間
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // 設置過期時間
                .signWith(SECRET_KEY)  // 使用密鑰進行簽名
                .compact();
    }

    // 解析 JWT 並獲取 email
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();  // 返回 email
    }

    // 驗證 JWT 是否有效
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            // 如果解析過程中出現問題，則視為無效
            return false;
        }
    }
}
