package com.freedy.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.freedy.backend.valid.Update;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 分类表
 * 
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-26 22:36:04
 */
@Data
@ApiModel("Category实体类")
@TableName("blog_category")
public class CategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull(message = "id不能为空",groups = Update.class)
	@ApiModelProperty("主键")
	@TableId(type = IdType.AUTO)
	private Integer id;

	@NotEmpty(message = "分类名称不能为空")
	@ApiModelProperty("分类名称")
	private String categoryName;

	@ApiModelProperty("图片url")
	private String categoryImgUrl;

	@ApiModelProperty("创建者")
	private Integer creatorId;

	@NotNull(message = "优先级不能为空")
	@ApiModelProperty("优先级 越小越大")
	private Integer priority;

}
