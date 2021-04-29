package com.freedy.backend.config.security;

import com.alibaba.fastjson.JSON;
import com.freedy.backend.common.utils.Result;
import com.freedy.backend.enumerate.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义无权访问处理类
 * @author Freedy
 * @date 2021/4/28 16:27
 */
@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e) throws IOException {
        log.debug(httpServletRequest.getRequestURI()+"无权访问!");
        Result r = Result.error(ResultCode.NO_PERMISSION.getCode(), ResultCode.NO_PERMISSION.getMessage());
        String json =  JSON.toJSONString(r);
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(json);
    }
}
