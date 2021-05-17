package com.freedy.backend.utils;

import com.freedy.backend.exception.EmailSendErrorException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Freedy
 * @date 2021/5/16 13:26
 */
@Slf4j
@Component
public class EmailSender {
    @Value("#{loadSetting.emailHostName}")
    private String emailHostName;
    @Value("#{loadSetting.emailFrom}")
    private  String emailFrom;
    @Value("#{loadSetting.emailAuthentication}")
    private  String emailAuthentication;
     @Value("#{loadSetting.sslPort}")
    private  Integer SSLPort;
     @Value("#{loadSetting.senderName}")
    private  String senderName;


    public void sendHtml(String sendToWho,String title,String content){
        log.debug("准备给{}发送邮件,标题是{}类容是{}",sendToWho,title,content);
        // 发送简单的email,不能添加附件
        HtmlEmail email=new HtmlEmail();
        // 邮件服务器域名
        email.setHostName(emailHostName);
        // 邮件服务器smtp协议的SSL端口
        email.setSmtpPort(SSLPort);
        // 用户名和密码为邮箱的账号和密码
        email.setAuthenticator(new DefaultAuthenticator(emailFrom, emailAuthentication));
        // SSL安全连接
        email.setSSLOnConnect(true);
        // 设置字符编码方式
        email.setCharset("UTF-8");
        // 发件人
        try {
            email.setFrom(emailFrom,senderName);
            // 收件人
            email.addTo(sendToWho);
            //title
            email.setSubject(title);
            // 邮件正文
            email.setMsg(content);
            // 发送
            email.send();
            log.debug("发送成功!");
        } catch (EmailException e) {
            e.printStackTrace();
            throw new EmailSendErrorException();
        }
    }

}
