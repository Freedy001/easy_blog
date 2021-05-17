package com.freedy.backend.entity.vo.search;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * @author Freedy
 * @date 2021/5/14 21:30
 */
@Data
@ApiModel("搜索结果实体类")
public class SearchResult {
    @ApiModelProperty("命中文章的标题")
    private String id;
    @ApiModelProperty("命中的标题")
    private String title;
    @ApiModelProperty("命中的标题")
    private String articleCategory;
    @ApiModelProperty("文章发布时间")
    private String publishTime;
    @ApiModelProperty("文章封面")
    private String articlePoster;
    @ApiModelProperty("命中的标题")
    private List<String> articleTags;
    @ApiModelProperty("命中的字段")
    private List<HitItem> hitItem;

    @Data
    public static class HitItem{
        @ApiModelProperty("命中字段")
        private String field;
        @ApiModelProperty("命中内容")
        private String content;
    }
}
