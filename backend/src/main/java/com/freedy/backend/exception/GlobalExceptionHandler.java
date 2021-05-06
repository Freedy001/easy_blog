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
     * 当用户尝试删除父表的值时
     * 该值已被子表所引用
     */
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public Result exceptionHandler(SQLIntegrityConstraintViolationException e) {
        log.error(e.getMessage());
        return Result.error(ResultCode.TAG_HAS_BEEN_USED.getCode(),ResultCode.TAG_HAS_BEEN_USED.getMessage())
                .put("reason",e.getMessage());
    }

    /**
     * 权限不足不能访问
     */
    @ExceptionHandler(value = NoPermissionsException.class)
    public Result exceptionHandler(NoPermissionsException e) {
        log.error(e.getMessage());
        return Result.error(ResultCode.NO_PERMISSION.getCode(),ResultCode.NO_PERMISSION.getMessage())
                .put("reason",e.getMessage());
    }

    /**
     * 权限不足不能访问
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public Result exceptionHandler(AccessDeniedException e) {
        log.error(e.getMessage());
        return Result.error(ResultCode.NO_PERMISSION.getCode(),ResultCode.NO_PERMISSION.getMessage())
                .put("reason",e.getMessage());
    }


    /**
     * 未知异常
     */
    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());
        log.error(e.getClass().getName());
        return Result.error(ResultCode.UNKNOWN_EXCEPTION.getCode(),ResultCode.UNKNOWN_EXCEPTION.getMessage())
                .put("reason",e.getMessage());
    }

}
