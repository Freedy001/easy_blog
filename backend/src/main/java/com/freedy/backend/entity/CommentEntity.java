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
 * 评论表
 * 
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-26 22:36:03
 */
@Data
@ApiModel("Comment实体类")
@TableName("blog_comment")
public class CommentEntity implements Serializable {
	private static final long serialVersionUID = 1L;


	@ApiModelProperty("主键")
	@TableId(type = IdType.AUTO)
	private Long id;

	@ApiModelProperty("文章id")
	private Long articleId;

	@ApiModelProperty("父评论id")
	private Long fatherCommentId;

	@ApiModelProperty("评论的楼数")
	private Integer flore;

	@ApiModelProperty("评论类容")
	private String content;

	@ApiModelProperty("评论人名称")
	private String username;

	@ApiModelProperty("评论人邮箱")
	private String email;

	@ApiModelProperty("评论人ip")
	private String ip;

	@ApiModelProperty("评论人地区")
	private String region;

	@ApiModelProperty("评论时间")
	private Long createTime;

}
