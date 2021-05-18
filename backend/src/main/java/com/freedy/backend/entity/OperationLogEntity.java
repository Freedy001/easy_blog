package com.freedy.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Freedy
 * @date 2021/5/18 11:42
 */
@Data
@ApiModel("Manager实体类")
@TableName("blog_operation_log")
public class OperationLogEntity {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("操作名称")
    private String operationName;
    @ApiModelProperty("操作者")
    private String operator;
    @ApiModelProperty("操作类型")
    private String operationType;
    @ApiModelProperty("是否操作成功 0成功 1失败")
    private Integer isSuccess;
    @ApiModelProperty("操作时间")
    private Long creatTime;

}
