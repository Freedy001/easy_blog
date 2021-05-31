package com.freedy.backend.entity.vo.setting;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Freedy
 * @date 2021/5/31 22:14
 */
@Data
public class OssSettingVo {
    @NotNull
    @ApiModelProperty("存储模式")
    private Boolean uploadMode; //true->本地  false->阿里云oss
    private String accessId;
    private String accessKey;
    private String endpoint;
    private String bucket;
}
