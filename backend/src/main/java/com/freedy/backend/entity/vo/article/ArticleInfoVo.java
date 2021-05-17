package com.freedy.backend.entity.vo.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Freedy
 * @date 2021/5/3 0:24
 */
@Data
@ApiModel("返回文章列表")
public class ArticleInfoVo implements Serializable {
    //这里数据类型设置为string是因为
    //当id为long时可能会导致前端id越界
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("文章状态")
    private Integer articleStatus;
    @ApiModelProperty("文章分类")
    private String articleCategory;
    @ApiModelProperty("文章描述")
    private String articleDesc;
    @ApiModelProperty("评论数")
    private int commentNum;
    @ApiModelProperty("访问数")
    private int visitNum;
    @ApiModelProperty("点赞数")
    private int likeNum;
    @ApiModelProperty("作者信息")
    private String authorName;
    @ApiModelProperty("文章字数")
    private Long wordNum;
    @ApiModelProperty("文章封面")
    private String articlePoster;
    @ApiModelProperty("发布时间")
    private String publishTime;
    @ApiModelProperty("修改时间")
    private String updateTime;
    @ApiModelProperty("文章标签")
    private List<String> articleTags;
}
