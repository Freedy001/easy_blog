package com.freedy.backend.aspect;

import com.freedy.backend.utils.Local;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author Freedy
 * @date 2021/5/26 13:38
 */
@Component
@Aspect
@Slf4j
public class ThreadLocalRemove {

    @AfterReturning("execution(* com.freedy.backend.api..*(..))")
    public void AfterReturning(){
        //清空threadLocal
        Local.MANAGER_LOCAL.remove();
        Local.PERMISSION_LOCAL.remove();
    }

}
