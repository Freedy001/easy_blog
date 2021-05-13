package com.freedy.backend.middleWare.es.model;

import static com.freedy.backend.constant.EsConstant.INDEX;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;


/**
 * @author Freedy
 * @date 2021/5/1 14:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("ES搜索实体类")
@Document(indexName =INDEX)
public class ArticleEsModel{

    @ApiModelProperty("主键")
    @Id
    private Long id;

    @ApiModelProperty("文章标题")
    @Field(type = FieldType.Text ,analyzer = "ik_max_word")
    private String title;

    @ApiModelProperty("文章状态")
    @Field(type = FieldType.Integer)
    private Integer articleStatus;

    @ApiModelProperty("分类名称")
    @Field(type = FieldType.Keyword)
    private String articleCategory;

    @ApiModelProperty("文章描述")
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String articleDesc;

    @ApiModelProperty("文章作者")
    @Field(type = FieldType.Keyword)
    private String authorName;

    @ApiModelProperty("文章封面")
    @Field(type = FieldType.Keyword,index = false)
    private String articlePoster;

    @ApiModelProperty("文章类容")
    @Field(type = FieldType.Text ,analyzer = "ik_max_word")
    private String content;

    @ApiModelProperty("文字数量")
    @Field(type = FieldType.Long)
    private Long wordNum;

    @ApiModelProperty("访问数量")
    @Field(type = FieldType.Long)
    private Long visitNum;

    @ApiModelProperty("评论数量")
    @Field(type = FieldType.Long)
    private Long commentNum;

    @ApiModelProperty("点赞数量")
    @Field(type = FieldType.Long)
    private Long likeNum;

    @ApiModelProperty("标签列表")
    @Field(type = FieldType.Keyword)
    private List<String> articleTags;

    @ApiModelProperty("文章发布时间")
    @Field(type = FieldType.Long,index = false)
    private Long publishTime;


}
