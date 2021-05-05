package com.freedy.backend.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Freedy
 * @date 2021/5/2 20:58
 */
@Data
@ConfigurationProperties(prefix = "easy.thread")
public class ThreadPoolConfigProperties {
    private Integer coreSize;
    private Integer maxSize;
    private Integer keepAliveTime;
}