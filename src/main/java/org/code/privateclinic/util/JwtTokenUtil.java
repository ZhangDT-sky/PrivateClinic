package org.code.privateclinic.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {

    @Value("{jwt.secret1}")
    private String secret1;

    @Value("{jwt.secret2}")
    private String secret2;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey secretKey1;
    private SecretKey secretKey2;


    /**
     * 初始化密钥
     */
    private SecretKey getSecretKey1() {
        if (secretKey1 == null) {
            secretKey1 = Keys.hmacShaKeyFor(secret1.getBytes(StandardCharsets.UTF_8));
        }
        return secretKey1;
    }

    private SecretKey getSecretKey2() {
        if (secretKey2 == null) {
            secretKey2 = Keys.hmacShaKeyFor(secret2.getBytes(StandardCharsets.UTF_8));
        }
        return secretKey2;
    }

    public String generateToken(String subject, Map<String, Object> claims) {
        if(claims==null){
            claims=new HashMap<>();
        }
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSecretKey1())
                .compact();
    }
    /**
     * 验证Token（双密钥验证：先尝试密钥1，失败则尝试密钥2）
     * @param token Token字符串
     * @return Claims对象，验证失败返回null
     */
    public Claims validateToken(String token) {
        // 先尝试使用密钥1验证
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey1())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            // 如果密钥1验证失败，尝试使用密钥2（支持密钥轮换）
            try {
                return Jwts.parser()
                        .verifyWith(getSecretKey2())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
            } catch (Exception ex) {
                return null;
            }
        }
    }

    /**
     * 验证Token是否有效
     * @param token Token字符串
     * @return true表示有效，false表示无效
     */
    public boolean isTokenValid(String token) {
        Claims claims = validateToken(token);
        return claims != null && !isTokenExpired(claims);
    }

    /**
     * 检查Token是否过期
     * @param claims Claims对象
     * @return true表示已过期，false表示未过期
     */
    public boolean isTokenExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    /**
     * 从Token中获取主题（用户ID或用户名）
     * @param token Token字符串
     * @return 主题字符串
     */
    public String getSubjectFromToken(String token) {
        Claims claims = validateToken(token);
        return claims != null ? claims.getSubject() : null;
    }

    /**
     * 从Token中获取Claims
     * @param token Token字符串
     * @return Claims对象
     */
    public Claims getClaimsFromToken(String token) {
        return validateToken(token);
    }

    /**
     * 从Token中获取指定声明
     * @param token Token字符串
     * @param key 声明键
     * @return 声明值
     */
    public Object getClaimFromToken(String token, String key) {
        Claims claims = validateToken(token);
        return claims != null ? claims.get(key) : null;
    }

    /**
     * 获取Token过期时间
     * @param token Token字符串
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = validateToken(token);
        return claims != null ? claims.getExpiration() : null;
    }

    /**
     * 判断Token是否即将过期（在指定秒数内过期）
     * @param token Token字符串
     * @param seconds 秒数
     * @return true表示即将过期
     */
    public boolean isTokenExpiringSoon(String token, long seconds) {
        Claims claims = validateToken(token);
        if (claims == null) {
            return true;
        }
        Date expiration = claims.getExpiration();
        long timeUntilExpiration = expiration.getTime() - System.currentTimeMillis();
        return timeUntilExpiration <= seconds * 1000;
    }
}
