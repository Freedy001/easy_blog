package com.freedy.backend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 角色表
 * 
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-26 22:36:03
 */
@Data
@ApiModel("Role实体类")
@TableName("blog_role")
public class RoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;


	@ApiModelProperty("主键")
	@TableId
	private Integer id;

	@ApiModelProperty("管理员id")
	private Integer manageId;

	@ApiModelProperty("权限id")
	private Integer permissionId;

	@ApiModelProperty("角色名称")
	private String roleName;

	@ApiModelProperty("角色状态 1:启用 0:停用")
	private Integer roleStatus;

}
