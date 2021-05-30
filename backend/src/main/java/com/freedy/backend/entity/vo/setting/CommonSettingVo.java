package com.freedy.backend.entity.vo.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author Freedy
 * @date 2021/5/16 16:18
 */
@Data
@ApiModel("常规设置实体类")
public class CommonSettingVo implements Serializable {

    private IndexArticle indexArticle;
    @NotEmpty(message = "博客标题不能为空")
    @ApiModelProperty("博客标题")
    private String blogTitle;
    @URL
    @ApiModelProperty("博客域名")
    private String webSiteDomainName;
    @NotEmpty(message = "logo不能为空")
    @ApiModelProperty("logo")
    private String logo;
    @NotEmpty(message = "首页图片不能为空")
    @ApiModelProperty("首页图片")
    private String poster;
    @NotEmpty(message = "首页色系不能为空")
    @ApiModelProperty("首页色系")
    private String indexColor;
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
