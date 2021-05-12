package com.freedy.backend.exception;

/**
 * @author Freedy
 * @date 2021/5/12 0:04
 */
public class NoIdException extends ParentResultException{
    public NoIdException() {
        super("NO_ID");
    }
}
