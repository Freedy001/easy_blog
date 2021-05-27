package com.freedy.backend.config.security;

import com.alibaba.fastjson.JSON;
import com.freedy.backend.constant.CacheConstant;
import com.freedy.backend.entity.OperationLogEntity;
import com.freedy.backend.enumerate.RecordEnum;
import com.freedy.backend.service.OperationLogService;
import com.freedy.backend.utils.Result;
import com.freedy.backend.enumerate.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/**
 * 登录失败操作
 * 当用户输入错误的账号或者密码时，就会进入这个处理类
 * @author Freedy
 * @date 2021/4/28 14:14
 */
@Slf4j
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private OperationLogService logService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        log.debug("登录失败!{}", exception.getMessage());
        Result error;
        if (exception.getMessage().matches("\\d*")){
            error = Result.error(exception.getMessage(), ResultCode.getMessageByCode(exception.getMessage()));
        }else {
            error = Result.error(exception.getMessage());
        }

        // 使用fastjson
        String json =  JSON.toJSONString(error);
        // 指定响应格式是json
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(json);
    }

}
