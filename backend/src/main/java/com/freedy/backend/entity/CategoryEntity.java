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


	@ApiModelProperty("主键")
	@TableId(type = IdType.AUTO)
	private Integer id;

	@ApiModelProperty("分类名称")
	private String categoryName;

	@ApiModelProperty("图片url")
	private String categoryImgUrl;

	@ApiModelProperty("优先级 越小越大")
	private Integer priority;

}
