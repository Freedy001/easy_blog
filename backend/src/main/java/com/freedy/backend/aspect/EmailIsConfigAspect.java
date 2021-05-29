package com.freedy.backend.aspect;

import com.freedy.backend.SysSetting.LoadSetting;
import com.freedy.backend.utils.Local;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Freedy
 * @date 2021/5/29 17:50
 */
@Slf4j
@Component
@Aspect
public class EmailIsConfigAspect {
    @Autowired
    private LoadSetting loadSetting;

    @Around("execution(* com.freedy.backend.Listenner.EmailListener.*(..))")
    public Object AfterReturning(ProceedingJoinPoint pjp) throws Throwable {
        if (loadSetting.getEmailHostName() == null || loadSetting.getEmailAuthentication() == null || loadSetting.getEmailFrom() == null) {
            log.warn("没有配置smtp服务，无法发送邮件");
            return null;
        } else {
            return pjp.proceed();
        }

    }
}
