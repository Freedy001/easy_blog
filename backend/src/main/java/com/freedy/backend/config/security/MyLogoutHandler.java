package com.freedy.backend.config.security;

import com.alibaba.fastjson.JSON;
import com.freedy.backend.common.utils.JwtTokenUtil;
import com.freedy.backend.common.utils.Result;
import com.freedy.backend.constant.RedisConstant;
import com.freedy.backend.enumerate.ResultCode;
import com.freedy.backend.properties.JwtProperties;
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
import java.io.IOException;

/**
 * @author Freedy
 * @date 2021/4/28 22:45
 */
@SuppressWarnings("ConstantConditions")
@Slf4j
@Component
public class MyLogoutHandler implements LogoutHandler {
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
        log.info("用户{}准备退出登录！",username);
    }
}