package com.freedy.backend.entity.vo.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author Freedy
 * @date 2021/5/2 18:53
 */
@Data
@ApiModel("Article保存文章实体类")
public class ArticleVo implements Serializable {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("文章类容")
    private String content;

    @ApiModelProperty("发布时间")
    private long publishTime;

    @ApiModelProperty("文章作者的id")
    private Integer authorId;

    @ApiModelProperty("是否开启评论")
    private Boolean isComment;//这里一定要使用包装类 不然传值时会发生数据错误

    @ApiModelProperty("是否顶置")
    private Boolean isOverhead;//这里一定要使用包装类 不然传值时会发生数据错误

    @ApiModelProperty("分类id")
    private Integer articleCategoryId;

    @ApiModelProperty("文章描述")
    private String articleDesc;

    @ApiModelProperty("图片url")
    private String articlePoster;

    @ApiModelProperty("要与之关联的teg->已存在的tegId")
    private List<Integer> existedTags;

    @ApiModelProperty("要与之关联的teg->要新建的tag")
    private List<String> notExistedTag;
}
