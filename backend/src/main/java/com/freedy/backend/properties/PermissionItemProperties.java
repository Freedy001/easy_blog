package com.freedy.backend.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Freedy
 * @date 2021/5/5 12:34
 */
@Data
@Component
@ConfigurationProperties(prefix = "easy.permission-item")
public class PermissionItemProperties implements Serializable {

    /**
     * articlePermission : ["可操作自己","可见他人","可操作他人"]
     * tagPermission : ["可操作自己","可见他人","可操作他人"]
     * commentPermission : ["可操作自己","可见他人","可操作他人"]
     * settingPermission : ["常规设置","评论设置","SMTP设置","附件设置"]
     */
    private Map<String, String> articlePermission;
    private Map<String, String> tagPermission;
    private Map<String, String> commentPermission;
    private Map<String, String> settingPermission;
}
