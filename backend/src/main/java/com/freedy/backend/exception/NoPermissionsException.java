package com.freedy.backend.exception;

/**
 * @author Freedy
 * @date 2021/5/6 16:25
 */
public class NoPermissionsException extends ParentResultException{
    public NoPermissionsException() {
        super("NO_PERMISSION");
    }
}
