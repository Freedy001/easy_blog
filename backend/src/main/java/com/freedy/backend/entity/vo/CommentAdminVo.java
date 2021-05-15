package com.freedy.backend.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Freedy
 * @date 2021/5/15 21:10
 */
@Data
@ApiModel("后台评论列表实体类")
public class CommentAdminVo implements Serializable {
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("类容")
    private String content;
    @ApiModelProperty("文章")
    private Article article;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("ip地址")
    private String ip;
    @ApiModelProperty("地区")
    private String region;
    @ApiModelProperty("父评论")
    private String fatherComment;
    @ApiModelProperty("创建时间")
    private String createTime;

    @Data
    public static class Article{
        @ApiModelProperty("文章id")
        private String id;
        @ApiModelProperty("文章标题")
        private String title;
    }
}
