package com.freedy.backend.aspect.annotation;

import java.lang.annotation.*;

/**
 * 用于删除es存储的类容
 *
 * @author Freedy
 * @date 2021/5/1 11:22
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ESEvict {

}
