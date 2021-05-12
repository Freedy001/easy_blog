package com.freedy.backend.exception;

import com.freedy.backend.common.utils.Result;
import com.freedy.backend.enumerate.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author Freedy
 * @date 2021/5/3 11:49
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 通用处理
     */
    @ExceptionHandler(value = ParentResultException.class)
    public Result exceptionHandler(ParentResultException e) {
        return handler(e.getMessage());
    }

    /**
     * 当用户尝试删除父表的值时
     * 该值已被子表所引用
     */
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public Result exceptionHandler(SQLIntegrityConstraintViolationException e) {
        return handler("TAG_HAS_BEEN_USED",e.getMessage());
    }

    /**
     * 权限不足不能访问
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public Result exceptionHandler(AccessDeniedException e) {
        return handler("NO_PERMISSION",e.getMessage());
    }


    /**
     * 未知异常
     */
    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(Exception e) {
        e.printStackTrace();
        return handler("UNKNOWN_EXCEPTION",e.getMessage());
    }


    private Result handler(String resultCodeName ,String reason){
        log.error(reason);
        return Result.error( ResultCode.valueOf(resultCodeName).getCode(), ResultCode.valueOf(resultCodeName).getMessage());
    }

    private Result handler(String resultCodeName){
        return Result.error( ResultCode.valueOf(resultCodeName).getCode(), ResultCode.valueOf(resultCodeName).getMessage());
    }

}
