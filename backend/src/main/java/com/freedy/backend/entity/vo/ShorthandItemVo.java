package com.freedy.backend.entity.vo;

import com.freedy.backend.entity.ShorthandEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Freedy
 * @date 2021/5/21 11:31
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ShorthandItemVo extends ShorthandEntity {
    private String nickname;
    private String publishTime;
}
