package com.freedy.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Freedy
 * @date 2021/5/6 23:00
 */
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = "file:"+System.getProperty("user.home")+"\\.easy\\image\\";
        String cssPath = "file:"+System.getProperty("user.home")+"\\.easy\\css\\";
        String resource = "classpath:/front-resource/";
        log.info("配置图片静态地址:{}",path);
        log.info("配置css静态地址:{}",cssPath);
        log.info("配置resource静态地址:{}",resource);
        registry.addResourceHandler("/image/**").addResourceLocations(path);
        registry.addResourceHandler("/css/**").addResourceLocations(cssPath);
        registry.addResourceHandler("/resource/**").addResourceLocations(resource);

    }
}
