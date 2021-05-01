package com.freedy.backend.middleWare.mq.aspect;

import com.freedy.backend.middleWare.mq.annotation.RefreshEsMqSender;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @author Freedy
 * @date 2021/5/1 11:24
 */
@Component
@Aspect
@Slf4j
public class RefreshEsMqAspect {
    @Pointcut("@annotation(com.freedy.backend.middleWare.mq.annotation.RefreshEsMqSender)")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        try {
            log.info("starting..........");
            Object result = pjp.proceed();
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            RefreshEsMqSender annotation = signature.getMethod().getAnnotation(RefreshEsMqSender.class);

            return pjp.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        } finally {
            System.out.println("ending..........");
        }
    }

}
