package com.freedy.backend.middleWare.mq.annotation;

import java.lang.annotation.*;

/**
 * @author Freedy
 * @date 2021/5/1 11:22
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RefreshEsMqSender {
    String sender();

    String msg() default "send refresh msg to ElasticSearch";

}
