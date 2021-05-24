package com.freedy.backend.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文章点赞数 评论数 和访问数
 * @author Freedy
 * @date 2021/5/25 1:43
 */
@Data
public class ArticleParameterDTO {
    @ApiModelProperty("访问数量")
    private Long visitNum;

    @ApiModelProperty("评论数量")
    private Long commentNum;

    @ApiModelProperty("点赞数量")
    private Long likeNum;
}
