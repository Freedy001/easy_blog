package com.freedy.backend.exception;

/**
 * @author Freedy
 * @date 2021/5/12 16:12
 */
public class ArgumentErrorException extends ParentResultException {
    public ArgumentErrorException() {
        super("ARGUMENT_ERROR");
    }
}
