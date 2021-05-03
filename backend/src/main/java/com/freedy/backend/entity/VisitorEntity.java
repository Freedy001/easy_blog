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
 * 访客记录表
 * 
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-26 22:36:03
 */
@Data
@ApiModel("Visitor实体类")
@TableName("blog_visitor")
public class VisitorEntity implements Serializable {
	private static final long serialVersionUID = 1L;


	@ApiModelProperty("主键")
	@TableId(type = IdType.AUTO)
	private Long id;

	@ApiModelProperty("访客ip")
	private String ipAddress;

	@ApiModelProperty("访客地域")
	private String region;

	@ApiModelProperty("访问时间")
	private String visitTime;

	@ApiModelProperty("访问时长")
	private String visitDuration;

}
