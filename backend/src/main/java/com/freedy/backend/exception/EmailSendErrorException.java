package com.freedy.backend.exception;

import com.freedy.backend.aspect.OperationLog;

/**
 * @author Freedy
 * @date 2021/5/16 14:48
 */
public class EmailSendErrorException extends RuntimeException{
    public EmailSendErrorException(Exception e) {
        super("邮件发送出错啦！");
        OperationLog.RecordLogManually("smtp服务出错==>"+e.getMessage(),false,"system");
    }
}
