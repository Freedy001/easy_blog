package com.freedy.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 系统设置表
 * 
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-26 22:36:03
 */
@Data
@ApiModel("Setting实体类")
@TableName("blog_setting")
public class SettingEntity implements Serializable {
	private static final long serialVersionUID = 1L;


	@ApiModelProperty("主键")
	@TableId(type = IdType.AUTO)
	private Integer id;

	@ApiModelProperty("设置项名称")
	private String itemName;

	@ApiModelProperty("设置项值")
	private String item;

}
