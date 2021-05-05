package com.freedy.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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


	@ApiModelProperty("主键")
	@TableId(type = IdType.AUTO)
	private Integer id;

	@ApiModelProperty("标签名称")
	private String tagName;

	@ApiModelProperty("标签图片---可为空")
	private String tagImgUrl;

	@ApiModelProperty("创建者")
	private Integer creatorId;

	@ApiModelProperty("越小优先级越高")
	private Integer priority;

}
