package com.freedy.backend.exception;

/**
 * @author Freedy
 * @date 2021/6/1 0:49
 */
public class MethodErrorException extends ParentResultException{
    public MethodErrorException() {
        super("METHOD_ERROR");
    }
}
