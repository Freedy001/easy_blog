package com.freedy.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Freedy
 * @date 2021/5/7 10:22
 */
@Data
@TableName("blog_resource")
@ApiModel("静态资源实体类")
public class ResourceEntity {
    @ApiModelProperty("主键")
    @TableId(type=IdType.AUTO)
    private Long id;
    @ApiModelProperty("静态资源地址")
    private String resourceUrl;
    @ApiModelProperty("创建时间")
    private Long createTime=new Date().getTime();

    public ResourceEntity(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }
}
