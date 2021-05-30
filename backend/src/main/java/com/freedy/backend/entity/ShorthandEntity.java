package com.freedy.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author Freedy
 * @date 2021/5/20 1:54
 */
@Data
@ApiModel("Setting实体类")
@TableName("blog_shorthand")
public class ShorthandEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    @NotEmpty
    private String content;
    private Long createTime;
    private Integer managerId;
}
