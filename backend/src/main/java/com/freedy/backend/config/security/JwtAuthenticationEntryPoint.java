package com.freedy.backend.config.security;

import com.alibaba.fastjson.JSON;
import com.freedy.backend.utils.Result;
import com.freedy.backend.enumerate.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 当用户没有携带有效凭证时，就会转到这里来
 * 认证失败处理类
 * @author Freedy
 * @date 2021/4/28 15:41
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        log.debug("访问无凭证");
        Result r;
        r = Result.error(ResultCode.USER_NO_CERTIFICATE_OR_ACCOUNT_EXPIRED.getCode()
                ,ResultCode.USER_NO_CERTIFICATE_OR_ACCOUNT_EXPIRED.getMessage());
        // 使用fastjson
        String json =  JSON.toJSONString(r);
        // 指定响应格式是json
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(json);
    }
}
