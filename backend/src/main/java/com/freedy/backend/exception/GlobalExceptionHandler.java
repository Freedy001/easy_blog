package com.freedy.backend.exception;

import com.freedy.backend.utils.Result;
import com.freedy.backend.enumerate.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindException;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

/**
 * @author Freedy
 * @date 2021/5/3 11:49
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 参数校验处理器
     */
    @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class})
    public Result validHandler(Exception e) {
        String msg = null;
        /// BindException
        if (e instanceof BindException) {
            // getFieldError获取的是第一个不合法的参数(P.S.如果有多个参数不合法的话)
            FieldError fieldError = ((BindException) e).getFieldError();
            if (fieldError != null) {
                msg = fieldError.getDefaultMessage();
            }
        } else if (e instanceof ConstraintViolationException) {
            /*
             * ConstraintViolationException的e.getMessage()形如
             *     {方法名}.{参数名}: {message}
             *  这里只需要取后面的message即可
             */
            msg = e.getMessage();
            if (msg != null) {
                int lastIndex = msg.lastIndexOf(':');
                if (lastIndex >= 0) {
                    msg = msg.substring(lastIndex + 1).trim();
                }
            }
            /// ValidationException 的其它子类异常
        } else {
            msg = ResultCode.ARGUMENT_ERROR.getMessage();
        }
        return handlerWithReason("ARGUMENT_ERROR",msg);
    }

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
    public Result exceptionHandler(DataIntegrityViolationException e) {
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
    @ExceptionHandler(value = Throwable.class)
    public Result exceptionHandler(Exception e) {
        e.printStackTrace();
        return handler("UNKNOWN_EXCEPTION",e.getMessage());
    }


    private Result handler(String resultCodeName ,String reason){
        log.error(reason);
        return Result.error( ResultCode.valueOf(resultCodeName).getCode(), ResultCode.valueOf(resultCodeName).getMessage());
    }

    private Result handlerWithReason(@SuppressWarnings("SameParameterValue") String resultCodeName , String reason){
        log.error(reason);
        return Result.error( ResultCode.valueOf(resultCodeName).getCode(), reason);
    }

    private Result handler(String resultCodeName){
        return Result.error( ResultCode.valueOf(resultCodeName).getCode(), ResultCode.valueOf(resultCodeName).getMessage());
    }

}
