package com.freedy.backend.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 *
 * @author Freedy
 * @date 2021/4/28 15:36
 */
@Data
@Component
@ConfigurationProperties(prefix = "easy.jwt")
public class JwtProperties {
    /** Request Headers ： Authorization */
    private String header;

    /** Base64对该令牌进行编码 */
    private String base64Secret;

    /** 令牌过期时间 此处单位 秒*/
    private Long tokenValidityInSeconds;

}
