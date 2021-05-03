package com.freedy.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Freedy
 * @date 2021/5/2 20:58
 */
@Data
@ConfigurationProperties("easy.thread")
public class ThreadPoolConfigProperties {
    private Integer coreSize;
    private Integer maxSize;
    private Integer keepAliveTime;
}