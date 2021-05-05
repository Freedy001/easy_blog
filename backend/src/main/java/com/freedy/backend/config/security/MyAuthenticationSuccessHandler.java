package com.freedy.backend.config.security;

import com.alibaba.fastjson.JSON;
import com.freedy.backend.common.utils.JwtTokenUtil;
import com.freedy.backend.common.utils.Result;
import com.freedy.backend.constant.RedisConstant;
import com.freedy.backend.entity.dto.UserTokenInfo;
import com.freedy.backend.enumerate.ResultCode;
import com.freedy.backend.properties.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 自定义认证成功处理器
 * 当用户认证成功之后，我们要在这里为用户生成token,
 * 并返回给用户，需要用到我们自定义的jwt工具类，也需要在配置类中配置
 * @author Freedy
 * @date 2021/4/28 14:14
 */
@Slf4j
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private JwtProperties properties;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String username = authentication.getName();
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String userInfo = ops.get(RedisConstant.USER_TOKEN_HEADER + username);
        //生成token
        String userToken = jwtTokenUtil.generateToken(username);
        //将用户信息与token存入redis
        UserTokenInfo info = JSON.parseObject(userInfo, UserTokenInfo.class);
        assert info != null;
        info.setToken(userToken);
        ops.set(RedisConstant.USER_TOKEN_HEADER+username,
                JSON.toJSONString(info),
                properties.getTokenValidityInSeconds(),
                TimeUnit.SECONDS);
        log.debug("生成token 用户:{} \ntoken:{}",username,userToken);
        Result token = Result.ok(
                ResultCode.LOGIN_SUCCESS.getCode(),
                ResultCode.LOGIN_SUCCESS.getMessage())
                .put("token", userToken);
        //将生成的authentication放入容器中，生成安全的上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String json = JSON.toJSONString(token);
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(json);
    }
}
