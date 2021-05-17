package com.freedy.backend.exception;

/**
 * @author Freedy
 * @date 2021/5/16 14:48
 */
public class EmailSendErrorException extends RuntimeException{
    public EmailSendErrorException() {
        super("邮件发送出错啦！");
    }
}
