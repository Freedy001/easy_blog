package com.freedy.backend.aspect;

import com.freedy.backend.constant.RabbitConstant;
import com.freedy.backend.entity.dto.EsTypeDto;
import com.freedy.backend.enumerate.EsType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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


    @SuppressWarnings("unchecked")
    @Around("@annotation(com.freedy.backend.aspect.annotation.ESEvict)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        try {
            log.debug("starting..........");
            Object[] args = pjp.getArgs();
            Object result = pjp.proceed(args);
            Long[] articleId = (Long[]) args[0];
            for (Long id : articleId) {
                EsTypeDto dto = new EsTypeDto();
                dto.setType(EsType.DELETE);
                dto.setId(id);
                template.convertAndSend(RabbitConstant.ES_EXCHANGE_NAME,
                        RabbitConstant.ES_ROUTE_KEY + ".refresh",
                        dto);
            }
            return result;
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        } finally {
            log.debug("ending..........");
        }
    }

}
