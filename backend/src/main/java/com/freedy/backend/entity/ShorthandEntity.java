package com.freedy.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author Freedy
 * @date 2021/5/20 1:54
 */
@Data
@ApiModel("Setting实体类")
@TableName("blog_shorthand")
public class ShorthandEntity {
    private Long id;
    private String content;
    private Long createTime;
    private Integer managerId;
}
