package com.freedy.backend.SysSetting;

import com.freedy.backend.entity.SettingEntity;
import com.freedy.backend.service.SettingService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Freedy
 * @date 2021/5/16 13:33
 */
@Data
@Component
@Slf4j
public class LoadSetting {
    private String setupTime;
    private String blogTitle;
    private String logo;
    private String poster;
    private String indexColor;
    private String footInfo;
    private String indexArticleIdAndTitle;//以逗号分割
    private String emailHostName;
    private String emailFrom;
    private String emailAuthentication;
    private String sslPort;
    private String senderName;
    private Boolean examination;//评论需要审核
    private Boolean newCommentNotification;//新评论通知作者
    private Boolean replayNotification;//评论回复通知对方
    private final SettingService service;

    LoadSetting(SettingService service) {
        this.service = service;
        refreshSetting();
    }

    /**
     * 重新从数据库加载数据
     */
    public void refreshSetting() {
        Map<String, String> settingMap = service.list().stream().collect(Collectors.toMap(SettingEntity::getItem, SettingEntity::getValue));
        for (Field field : LoadSetting.class.getDeclaredFields()) {
            String fieldName = field.getName();
            String value = settingMap.get(fieldName);
            if (StringUtils.hasText(value)) {
                field.setAccessible(true);
                try {
                    if (value.equals("true") || value.equals("false")) {
                        field.set(this, Boolean.parseBoolean(value));
                    } else {
                        field.set(this, value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                log.info("加载系统设置{},值为{}", fieldName, value);
            } else {
                if (!fieldName.equals("log") && !fieldName.equals("service"))//排除掉sl4j的log
                    log.warn("数据库中没有初始值{}", fieldName);
            }
        }
    }


}
