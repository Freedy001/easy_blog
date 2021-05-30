package com.freedy.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.freedy.backend.valid.Update;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 文章标签
 * 
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-26 22:36:04
 */
@Data
@ApiModel("Tag实体类")
@TableName("blog_tag")
public class TagEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull(message = "id不能为空",groups = Update.class)
	@ApiModelProperty("主键")
	@TableId(type = IdType.AUTO)
	private Integer id;

	@NotEmpty
	@ApiModelProperty("标签名称")
	private String tagName;

	@ApiModelProperty("标签图片---可为空")
	private String tagImgUrl;

	@ApiModelProperty("创建者")
	private Integer creatorId;

	@NotNull
	@ApiModelProperty("越小优先级越高")
	private Integer priority;

}
