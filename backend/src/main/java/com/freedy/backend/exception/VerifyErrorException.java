package com.freedy.backend.exception;

/**
 * @author Freedy
 * @date 2021/5/21 21:34
 */
public class VerifyErrorException extends ParentResultException{
    public VerifyErrorException() {
        super("VERIFY_ERROR");
    }
}
