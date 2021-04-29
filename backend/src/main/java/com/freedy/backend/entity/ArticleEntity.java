package com.freedy.backend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * 文章表
 * 
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-26 22:36:04
 */
@Data
@ApiModel("Article实体类")
@TableName("blog_article")
public class ArticleEntity implements Serializable {
	private static final long serialVersionUID = 1L;


	@ApiModelProperty("主键")
	@TableId
	private Long id;

	@ApiModelProperty("文章标题")
	private String title;

	@ApiModelProperty("文章描述")
	private String articleDesc;

	@ApiModelProperty("文章作者")
	private String authorName;

	@ApiModelProperty("文章封面")
	private String articlePoster;

	@ApiModelProperty("文章类容")
	private String content;

	@ApiModelProperty("访问数量")
	private Long visitNum;

	@ApiModelProperty("评论数量")
	private Integer commentNum;

	@ApiModelProperty("点赞数量")
	private Integer likeNum;

	@ApiModelProperty("文章状态 0:未发布 1:已发布 2:顶置 3:推荐 4:回收站")
	private Integer articleStatus;

	@ApiModelProperty("分类id")
	private Integer articleCategoryId;

	@ApiModelProperty("文章创建时间")
	private Long createTime;

	@ApiModelProperty("文章更改时间")
	private Long updateTime;

}
