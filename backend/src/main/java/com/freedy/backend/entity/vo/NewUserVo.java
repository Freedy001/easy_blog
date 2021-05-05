package com.freedy.backend.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Freedy
 * @date 2021/5/5 13:42
 */
@ApiModel("创建新用户的实体类")
@Data
public class NewUserVo implements Serializable {
    @ApiModelProperty("新用户的用户名")
    private String username;
    @ApiModelProperty("新用户的密码")
    private String password;
    @ApiModelProperty("新用户的邮箱")
    private String email;
    @ApiModelProperty("文章权限")
    private List<String> articlePermission;
    @ApiModelProperty("标签权限")
    private List<String> tagPermission;
    @ApiModelProperty("评论权限")
    private List<String> commentPermission;
    @ApiModelProperty("设置权限")
    private List<String> settingPermission;
}
