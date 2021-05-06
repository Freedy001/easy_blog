package com.freedy.backend.config;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Freedy
 * @date 2021/5/6 23:00
 */
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = System.getProperty("user.home");
        registry.addResourceHandler("/image/**").addResourceLocations("file:"+path);
    }
}
