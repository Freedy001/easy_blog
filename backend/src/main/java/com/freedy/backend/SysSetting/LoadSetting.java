package com.freedy.backend.SysSetting;

import com.alibaba.fastjson.JSON;
import com.freedy.backend.entity.SettingEntity;
import com.freedy.backend.service.ManagerService;
import com.freedy.backend.service.SettingService;
import com.freedy.backend.utils.RunSqlScript;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.Permission;
import java.util.*;
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
    private String webSiteDomainName;
    @Autowired
    private SettingService service;
    @Autowired
    private ManagerService managerService;


    @PostConstruct
    public void init() {
        refreshSetting();
        if (managerService.count() == 0) {
            log.warn("在数据库中没有发现管理员,开始创建根管理员.....");
            managerService.createRootUser();
            log.warn("创建成功！！！");
            log.warn("初始用户名:root");
            log.warn("初始密码:123456");
        }
    }

    /**
     * 重新从数据库加载数据
     */
    public void refreshSetting() {
        Map<String, String> settingMap = service.list().stream().collect(Collectors.toMap(SettingEntity::getItem, SettingEntity::getValue));
        Field[] fields = LoadSetting.class.getDeclaredFields();
        if (settingMap.keySet().size() < fields.length - 4) {//排除掉sl4j的log 和service
            initSetting();
            return;
        }
        for (Field field : fields) {
            String fieldName = field.getName();
            String value = settingMap.get(fieldName);
            if (StringUtils.hasText(value)) {
                field.setAccessible(true);
                try {
                    if ("true".equals(value) || "false".equals(value)) {
                        field.set(this, Boolean.parseBoolean(value));
                    } else {
                        field.set(this, value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                log.info("加载系统设置{},值为{}", fieldName, value);
            } else {
                //排除掉sl4j的log
                if (!"log".equals(fieldName) && !"service".equals(fieldName) && !"managerService".equals(fieldName)) {
                    log.debug("没有发现系统设置{}", fieldName);
                }
            }
        }
    }

    private void initSetting() {
        try {
            Class<? extends LoadSetting> currentClazz = LoadSetting.class;
            InputStream fis = currentClazz.getClassLoader().getResourceAsStream("ini.json");
            String iniData = new String(Objects.requireNonNull(fis).readAllBytes());
            LoadSetting loadSetting = JSON.parseObject(iniData, currentClazz);
            ArrayList<SettingEntity> list = new ArrayList<>();
            for (Field field : currentClazz.getDeclaredFields()) {
                String fieldName = field.getName();
                if (!"log".equals(fieldName) && !fieldName.toLowerCase(Locale.ROOT).contains("service")) {
                    SettingEntity entity = new SettingEntity();
                    entity.setItem(fieldName);
                    String value;
                    if ("setupTime".equals(fieldName)) {
                        value = String.valueOf(System.currentTimeMillis());
                    } else {
                        value = String.valueOf(field.get(loadSetting));
                    }
                    entity.setValue(value);
                    list.add(entity);
                    log.info("初始化系统设置{}-->{}", fieldName, value);
                }
            }
            if (list.size() != 0) {
                service.saveBatch(list);
                refreshSetting();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
