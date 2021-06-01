package com.freedy.backend.config;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
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
        String path = "file:"+System.getProperty("user.home")+"/.easy/image/";
        String adminPath = "classpath:/admin/";
        String appPath = "classpath:/app/";
        String resource = "classpath:/front-resource/";
        log.info("配置图片静态地址:{}",path);
        log.info("配置ADMIN静态地址:{}",adminPath);
        log.info("配置APP静态地址:{}",appPath);
        log.info("配置resource静态地址:{}",resource);
        registry.addResourceHandler("/image/**").addResourceLocations(path);
        registry.addResourceHandler("/admin/**").addResourceLocations(adminPath);
        registry.addResourceHandler("/**").addResourceLocations(appPath);
        registry.addResourceHandler("/resource/**").addResourceLocations(resource);
    }

    @Override
    public void addViewControllers(@NotNull ViewControllerRegistry registry) {
        registry.addViewController("/admin").setViewName("/admin/index.html");
        registry.addViewController("/").setViewName("/index.html");
    }


}
