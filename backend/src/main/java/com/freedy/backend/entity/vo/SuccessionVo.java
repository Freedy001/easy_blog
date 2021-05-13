package com.freedy.backend.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Freedy
 * @date 2021/5/13 21:11
 */
@Data
@ApiModel("搜索建议实体类")
public class SuccessionVo {
    @ApiModelProperty("命中字段")
    private String field;
    @ApiModelProperty("命中类容")
    private String content;
    @ApiModelProperty("命中的标题")
    private String title;
}
