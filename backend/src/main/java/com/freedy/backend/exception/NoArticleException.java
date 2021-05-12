package com.freedy.backend.exception;

/**
 * @author Freedy
 * @date 2021/5/11 12:02
 */
public class NoArticleException extends ParentResultException {
    public NoArticleException() {
        super("NO_ARTICLE");
    }
}
