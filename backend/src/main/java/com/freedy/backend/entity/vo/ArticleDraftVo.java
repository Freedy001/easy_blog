package com.freedy.backend.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Freedy
 * @date 2021/5/3 21:09
 */
@Data
@ApiModel("草稿文章实体类")
public class ArticleDraftVo {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("文章类容")
    private String content;
}
