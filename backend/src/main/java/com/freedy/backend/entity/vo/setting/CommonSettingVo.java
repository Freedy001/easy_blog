package com.freedy.backend.entity.vo.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Freedy
 * @date 2021/5/16 16:18
 */
@Data
@ApiModel("常规设置实体类")
public class CommonSettingVo implements Serializable {

    private IndexArticle indexArticle;
    @ApiModelProperty("博客标题")
    private String blogTitle;
    @ApiModelProperty("logo")
    private String logo;
    @ApiModelProperty("首页图片")
    private String poster;
    @ApiModelProperty("首页色系")
    private String indexColor;
    @ApiModelProperty("页角信息")
    private String footInfo;

    @Data
    public static class IndexArticle implements Serializable {
        @ApiModelProperty("文章id")
        private String id;
        @ApiModelProperty("文章标题")
        private String title;
        @ApiModelProperty("发布时间")
        private String publishTime;
        @ApiModelProperty("文章描述")
        private String articleDesc;
    }
}
