package com.freedy.backend.middleWare.mq.aspect;

import com.freedy.backend.constant.RabbitConstant;
import com.freedy.backend.middleWare.mq.annotation.StringSender;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Freedy
 * @date 2021/5/1 11:24
 */
@Component
@Aspect
@Slf4j
public class RefreshEsMqAspect {

    @Autowired
    private RabbitTemplate template;

    @Pointcut("@annotation(com.freedy.backend.middleWare.mq.annotation.StringSender)")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        try {
            log.debug("starting..........");
            Object result = pjp.proceed();
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            StringSender annotation = signature.getMethod().getAnnotation(StringSender.class);
            log.debug(annotation.msg());
            template.convertAndSend(RabbitConstant.ES_EXCHANGE_NAME,
                    RabbitConstant.ES_ROUTE_KEY+".refresh",
                    annotation.msg());
            return result;
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        } finally {
            log.debug("ending..........");
        }
    }

}
