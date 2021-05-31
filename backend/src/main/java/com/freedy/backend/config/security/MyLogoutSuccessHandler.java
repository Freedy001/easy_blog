package com.freedy.backend.config.security;

import com.alibaba.fastjson.JSON;
import com.freedy.backend.aspect.OperationLog;
import com.freedy.backend.aspect.annotation.RecordLog;
import com.freedy.backend.constant.CacheConstant;
import com.freedy.backend.entity.OperationLogEntity;
import com.freedy.backend.enumerate.RecordEnum;
import com.freedy.backend.exception.ParentResultException;
import com.freedy.backend.service.OperationLogService;
import com.freedy.backend.utils.JwtTokenUtil;
import com.freedy.backend.utils.Local;
import com.freedy.backend.utils.Result;
import com.freedy.backend.constant.RedisConstant;
import com.freedy.backend.enumerate.ResultCode;
import com.freedy.backend.properties.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/**
 * @author Freedy
 * @date 2021/4/28 22:51
 */
@SuppressWarnings("ConstantConditions")
@Slf4j
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private JwtTokenUtil util;

    @Autowired
    private OperationLogService logService;


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException{
        log.debug("退出登录");
        String token = request.getHeader(jwtProperties.getHeader());
        String username = util.getUsernameFromToken(token);
        log.debug("logout header Token {}", token);
        log.debug("logout request method{}", request.getMethod());
        if (redisTemplate.delete(RedisConstant.USER_TOKEN_HEADER+username)) {
            log.debug("authentication {}", authentication);
            log.debug("logout success!");
            SecurityContextHolder.clearContext();
            String json =  JSON.toJSONString(Result.ok(ResultCode.SUCCESS.getCode(),
                    ResultCode.SUCCESS.getMessage()));
            // 指定响应格式是json
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(json);
            OperationLog.RecordLogManually("用户退出登录",true,username);
        } else {
            OperationLog.RecordLogManually("退出登录失败",true,username);
            throw new ParentResultException("LOGOUT_ERROR");
        }
    }


}
