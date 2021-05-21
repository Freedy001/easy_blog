package com.freedy.backend.exception;

/**
 * @author Freedy
 * @date 2021/5/21 12:35
 */
public class WordOutOfLimitException extends ParentResultException{
    public WordOutOfLimitException() {
        super("OUT_OF_LIMIT");
    }
}
