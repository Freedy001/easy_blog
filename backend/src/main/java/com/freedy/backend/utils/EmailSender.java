package com.freedy.backend.utils;

import com.freedy.backend.SysSetting.LoadSetting;
import com.freedy.backend.aspect.OperationLog;
import com.freedy.backend.exception.EmailSendErrorException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Freedy
 * @date 2021/5/16 13:26
 */
@Slf4j
@Component
public class EmailSender {

    @Autowired
    private LoadSetting loadSetting;

    public void sendHtml(String sendToWho, String title, String content) {
        try {
            log.debug("准备给{}发送邮件,标题是{}类容是{}", sendToWho, title, content);
            // 发送简单的email,不能添加附件
            HtmlEmail email = new HtmlEmail();
            // 邮件服务器域名
            email.setHostName(loadSetting.getEmailHostName());
            // 邮件服务器smtp协议的SSL端口
            email.setSmtpPort(Integer.parseInt(loadSetting.getSslPort()));
            // 用户名和密码为邮箱的账号和密码
            email.setAuthenticator(new DefaultAuthenticator(loadSetting.getEmailFrom(), loadSetting.getEmailAuthentication()));
            // SSL安全连接
            email.setSSLOnConnect(true);
            // 设置字符编码方式
            email.setCharset("UTF-8");
            // 发件人
            email.setFrom(loadSetting.getEmailFrom(), loadSetting.getSenderName());
            // 收件人
            email.addTo(sendToWho);
            //title
            email.setSubject(title);
            // 邮件正文
            email.setMsg(content);
            // 发送
            email.send();
            log.debug("发送成功!");
        } catch (NullPointerException e) {
            log.error("没有配置smtp服务，无法发送邮件");
            OperationLog.RecordLogManually("没有配置smtp服务，无法发送邮件", false, "system");
        } catch (Exception e) {
            log.error("邮件发送出错==>{}", e.getMessage());
            OperationLog.RecordLogManually("smtp服务出错==>" + e.getMessage(), false, "system");
        }
    }

}
