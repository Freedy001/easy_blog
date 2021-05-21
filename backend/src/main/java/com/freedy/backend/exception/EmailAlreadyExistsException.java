package com.freedy.backend.exception;

/**
 * @author Freedy
 * @date 2021/5/21 22:36
 */
public class EmailAlreadyExistsException extends ParentResultException{
    public EmailAlreadyExistsException() {
        super("EMAIL_ALREADY_EXIST");
    }
}
