package com.freedy.backend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 标签文章关联表
 * 
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-26 22:36:04
 */
@Data
@ApiModel("ArticleTagRelation实体类")
@TableName("blog_article_tag_relation")
public class ArticleTagRelationEntity implements Serializable {
	private static final long serialVersionUID = 1L;


	@ApiModelProperty("主键")
	@TableId
	private Integer id;

	@ApiModelProperty("标签id")
	private Integer tagId;

	@ApiModelProperty("文章id")
	private Long articleId;

}
