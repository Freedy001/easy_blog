package com.freedy.backend.config.security;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Freedy
 * @date 2021/4/28 15:36
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    /** Request Headers ： Authorization */
    private String header;

    /** Base64对该令牌进行编码 */
    private String base64Secret;

    /** 令牌过期时间 此处单位/毫秒 */
    private Long tokenValidityInSeconds;

}
