package com.freedy.backend.config.security;

import com.freedy.backend.common.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Freedy
 * @date 2021/4/28 22:45
 */
@SuppressWarnings("ConstantConditions")
@Slf4j
@Component
public class MyLogoutHandler implements LogoutHandler {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private JwtTokenUtil util;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        String token = request.getHeader(jwtProperties.getHeader());
        String username = util.getUsernameFromToken(token);
        if(redisTemplate.delete(username)){
            log.debug("logout header Token {}",token);
            log.debug("logout request method{}",request.getMethod());
            if (StringUtils.hasText(token)) {
                log.debug("authentication {}",authentication);
                SecurityContextHolder.clearContext();
            }
        }
    }
}