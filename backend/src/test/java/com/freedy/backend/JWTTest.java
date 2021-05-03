package com.freedy.backend;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;

/**
 * @author Freedy
 * @date 2021/4/28 15:27
 */
public class JWTTest {

    @Test
    public void test1() {
        String token = Jwts.builder()
                //主题 放入用户名
                .setSubject("niceyoo")
                //自定义属性 放入用户拥有请求权限
                .claim("authorities", "admin")
                //失效时间
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                //签名算法和密钥
                .signWith(SignatureAlgorithm.HS512, "tmax")
                .compact();
        System.out.println(token);
    }
    @Test
    public void test2(){
        try {
            //解析token
            Claims claims = Jwts.parser()
                    .setSigningKey("sanbayan")
                    .parseClaimsJws("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmcmVlZHkiLCJjcmVhdGVkIjoxNjE5ODc3MTMxNTA1LCJleHAiOjE2MTk4OTE1MzF9.a-zDaW7b5PpMaHtlKENrsQEvFeD-L8Tka2K-Y9k-QHNb2N3gsIvJdw_bHALrNrdWX-f5K3L_Lbo3VcApBVlf2A")
                    .getBody();

            System.out.println(claims);
            //获取用户名
            String username = claims.getSubject();
            System.out.println("username:"+username);
            //获取权限
            String authority = claims.get("authorities").toString();
            System.out.println("权限："+authority);
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            System.out.println("jwt异常");
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("异常");
        }
    }

    @Test
    public void test3(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode("123456");
        System.out.println(encoder.matches("123456", "$2a$10$./hC1bNlVDtdVkqMrJuqE.RNNqRq1KQtSS0w0OubXCZGuN367yEiy"));
        System.out.println(encode);
    }

}
