package com.freedy.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 管理员表
 * 
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-26 22:36:03
 */
@Data
@ApiModel("Manager实体类")
@TableName("blog_manager")
public class ManagerEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty("主键")
	@TableId(type = IdType.AUTO)
	private Integer id;
	@ApiModelProperty("昵称")
	private String nickname;
	@ApiModelProperty("用户名")
	private String username;
	@ApiModelProperty("密码")
	private String password;
	@ApiModelProperty("邮箱")
	private String email;
	@ApiModelProperty("个人说明")
	private String introduce;
	@ApiModelProperty("头像")
	private String headImg;
	@ApiModelProperty("创建时间")
	private Long createTime;
	@ApiModelProperty("更新时间")
	private Long updateTime;
	//状态 1主管理员 2启用 3启用
	@ApiModelProperty("状态")
	private Integer status;
}
