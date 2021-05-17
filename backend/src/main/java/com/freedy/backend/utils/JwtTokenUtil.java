package com.freedy.backend.utils;

import com.freedy.backend.properties.JwtProperties;
import com.freedy.backend.entity.dto.UserTokenInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT生成令牌、验证令牌、获取令牌
 *
 * @author Freedy
 * @date 2021/4/28 14:25
 */
@Component
public class JwtTokenUtil {
    // 注入自己的jwt配置
    @Autowired
    private JwtProperties jwtProperties;

    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_CREATED = "created";


    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtProperties.getBase64Secret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    //设置过期时间
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + jwtProperties.getTokenValidityInSeconds()*1000);
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, username);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 生成token（最关键）
     */
    String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)  //设置声明信息（用户名等）
                .setExpiration(generateExpirationDate()) //设置过期时间
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getBase64Secret()) //设置签名
                .compact();
    }

    //TODO,验证当前的token是否有效
    public Boolean validateToken(String token, UserTokenInfo userTokenInfo) {
        return userTokenInfo.getToken().equals(token);
    }

}