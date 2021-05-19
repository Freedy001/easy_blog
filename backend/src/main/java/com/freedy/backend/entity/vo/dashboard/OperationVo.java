package com.freedy.backend.entity.vo.dashboard;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 操作日志实体类
 * @author Freedy
 * @date 2021/5/19 17:35
 */
@Data
@ApiModel("操作日志实体类")
public class OperationVo {
    @ApiModelProperty("操作名称")
    private String operationName;
    @ApiModelProperty("操作者")
    private String operator;
    @ApiModelProperty("操作时间")
    private String creatTime;
    @ApiModelProperty("操作状态")
    private String operationStatus;
}
