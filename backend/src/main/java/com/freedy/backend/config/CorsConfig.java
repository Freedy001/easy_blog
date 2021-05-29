package com.freedy.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.function.Predicate;

/**
 * @author Freedy
 * @date 2021/4/28 12:44
 */
@Slf4j
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Autowired
    private Environment environment;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
       if (environment.acceptsProfiles(Profiles.of("dev"))){
           log.info("--->开启跨域");
           // 设置允许跨域的路由
           registry.addMapping("/**")
                   // 设置允许跨域请求的域名
                   .allowedOriginPatterns("*")
                   // 是否允许证书（cookies）
                   .allowCredentials(true)
                   // 设置允许的方法
                   .allowedMethods("*")
                   // 跨域允许时间
                   .maxAge(3600);
       }
    }

}