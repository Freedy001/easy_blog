package com.freedy.backend.exception;

/**
 * @author Freedy
 * @date 2021/5/27 10:05
 */
public class FileDeleteErrorException extends ParentResultException{
    public FileDeleteErrorException() {
        super("FILE_DELETE_ERROR");
    }
}
