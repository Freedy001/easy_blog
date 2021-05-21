package com.freedy.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Freedy
 * @date 2021/5/15 20:26
 */
@Data
@ApiModel("Setting实体类")
@TableName("blog_subscriber")
public class SubscriberEntity {
    @ApiModelProperty("id")
    @TableId(type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty("订阅者的邮箱")
    private String subscriberEmail;
    @ApiModelProperty("创建时间")
    private Long createTime;

    //uuid唯一标识
    @TableField(exist = false)
    private String UUID;
}
