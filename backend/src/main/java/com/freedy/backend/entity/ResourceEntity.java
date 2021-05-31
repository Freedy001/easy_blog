package com.freedy.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Freedy
 * @date 2021/5/7 10:22
 */
@NoArgsConstructor
@Data
@TableName("blog_resource")
@ApiModel("静态资源实体类")
public class ResourceEntity {
    @ApiModelProperty("主键")
    @TableId(type=IdType.AUTO)
    private Long id;
    @ApiModelProperty("静态资源地址")
    private String resourceUrl;
    @ApiModelProperty("上传者id")
    private Integer creatorId;
    @ApiModelProperty("资源类型 0本地 1阿里云oss")
    private Integer resourceType;
    @ApiModelProperty("创建时间")
    private Long createTime=System.currentTimeMillis();

    public ResourceEntity(String resourceUrl,Integer creatorId,Integer resourceType) {
        this.resourceUrl = resourceUrl;
        this.creatorId = creatorId;
        this.resourceType = resourceType;
    }
}
