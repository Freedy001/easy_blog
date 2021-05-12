package com.freedy.backend.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Freedy
 * @date 2021/5/11 23:01
 */
@Data
@ApiModel("发布评论实体类")
public class CommentVo implements Serializable {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("父评论人的名字")
    private String parentName;
    @ApiModelProperty("评论人名称")
    private String username;
    @ApiModelProperty("类容")
    private String content;
    @ApiModelProperty("评论时间")
    private String createTime;
    @ApiModelProperty("子评论")
    private List<CommentVo> childComment=new ArrayList<>();
}
