package com.freedy.backend.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

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
@Document(indexName = "article")
public class ArticleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("主键")
	@TableId(type = IdType.AUTO)
	private Long id;

	@ApiModelProperty("文章标题")
	private String title;

	@ApiModelProperty("文章描述")
	private String articleDesc;

	@ApiModelProperty("文章作者的id")
	private Integer authorId;

	@ApiModelProperty("文章封面")
	private String articlePoster;

	@ApiModelProperty("文章类容")
	private String content;

	@ApiModelProperty("访问数量")
	private Long visitNum;

	@ApiModelProperty("评论数量")
	private Long commentNum;

	@ApiModelProperty("点赞数量")
	private Long likeNum;

	@ApiModelProperty("文章状态 0:未发布 1:已发布 2:顶置 3:推荐 4:回收站")
	private Integer articleStatus;

	@ApiModelProperty("文章顶置 0:可以评论 1:不能评论")
	private Integer articleComment;

	@ApiModelProperty("分类id")
	private Integer articleCategoryId;

	@ApiModelProperty("文章创建时间")
	private Long createTime;

	@ApiModelProperty("文章更改时间")
	private Long updateTime;

	@ApiModelProperty("发布时间")
	private Long publishTime;


}
