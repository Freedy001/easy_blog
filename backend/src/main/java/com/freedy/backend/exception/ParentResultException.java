package com.freedy.backend.exception;

/**
 * @author Freedy
 * @date 2021/5/12 0:06
 */
public class ParentResultException extends RuntimeException{
    public ParentResultException() {
    }

    public ParentResultException(String message) {
        super(message);
    }

    public ParentResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParentResultException(Throwable cause) {
        super(cause);
    }

    public ParentResultException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
