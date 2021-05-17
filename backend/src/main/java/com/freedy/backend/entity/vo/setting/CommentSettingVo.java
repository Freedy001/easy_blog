package com.freedy.backend.entity.vo.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Freedy
 * @date 2021/5/16 23:21
 */
@Data
@ApiModel("评论设置实体类")
public class CommentSettingVo{
    @ApiModelProperty("评论需要审核")
    private Boolean examination;
    @ApiModelProperty("新评论通知")
    private Boolean newCommentNotification;
    @ApiModelProperty("评论回复通知对方")
    private Boolean replayNotification;
}
