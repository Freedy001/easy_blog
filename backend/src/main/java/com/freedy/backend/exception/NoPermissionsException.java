package com.freedy.backend.exception;

/**
 * @author Freedy
 * @date 2021/5/6 16:25
 */
public class NoPermissionsException extends RuntimeException{
    public NoPermissionsException() {
        super("无权限!");
    }

    public NoPermissionsException(String message) {
        super(message);
    }

    public NoPermissionsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPermissionsException(Throwable cause) {
        super(cause);
    }

    public NoPermissionsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
