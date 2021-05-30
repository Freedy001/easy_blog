package com.freedy.backend.entity.vo.manager;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * @author Freedy
 * @date 2021/5/4 22:16
 */
@Data
@ApiModel("修改用户密码的实体类")
public class UserPasswordVo {

    @NotEmpty
    @Length(min = 8,max = 20)
    @ApiModelProperty("旧密码")
    private String oldPassword;

    @NotEmpty
    @Length(min = 8,max = 20)
    @ApiModelProperty("新密码")
    private String newPassword;
}
