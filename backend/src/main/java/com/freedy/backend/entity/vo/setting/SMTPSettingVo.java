package com.freedy.backend.entity.vo.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Freedy
 * @date 2021/5/16 22:40
 */
@Data
@ApiModel("SMTP设置实体类")
public class SMTPSettingVo implements Serializable {
    @ApiModelProperty("smtp地址")
    private String emailHostName;
    @ApiModelProperty("发件人邮箱")
    private String emailFrom;
    @ApiModelProperty("邮箱密码或授权码")
    private String emailAuthentication;
    @ApiModelProperty("SSL端口号")
    private String sslPort;
    @ApiModelProperty("发送者的名称")
    private String senderName;
}
