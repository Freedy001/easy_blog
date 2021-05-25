package com.freedy.backend.aspect.annotation;

import com.freedy.backend.enumerate.RecordEnum;

import java.lang.annotation.*;

/**
 * 用于记录日志
 *
 * @author Freedy
 * @date 2021/5/18 10:22
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RecordLog {
    RecordEnum type();

    /**
     * 当给出该值时，日志记录会关闭AutoGuess功能
     * 并使用你给出的值作为操作日志的类容
     */
    String logMsg() default "";

}
