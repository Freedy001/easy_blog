package com.freedy.backend.common.utils;

import com.freedy.backend.config.security.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * 获取用户消息的工具类
 * @author Freedy
 * @date 2021/5/3 22:07
 */
@Component
public class AuthorInfoUtil {
    private static JwtProperties jwtProperties;
    private static JwtTokenUtil util;

    /**
     * 为JwtProperties与JwtTokenUtil初始化值
     * 因为他们是静态变量所以不能用Autowired注入
     */
    @Autowired
    public void init(JwtProperties jwtProperties,JwtTokenUtil util){
        AuthorInfoUtil.jwtProperties =jwtProperties;
        AuthorInfoUtil.util =util;
    }

    /**
     * 获取用户名
     */
    public static String getCurrentUsername(){
        String token = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                .getRequest().getHeader(jwtProperties.getHeader());
        return util.getUsernameFromToken(token);
    }


}
