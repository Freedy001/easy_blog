package com.freedy.backend.entity.vo.manager;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Freedy
 * @date 2021/5/4 22:16
 */
@Data
@ApiModel("修改用户密码的实体类")
public class UserPasswordVo {
    @ApiModelProperty("旧密码")
    private String oldPassword;
    @ApiModelProperty("新密码")
    private String newPassword;
}
