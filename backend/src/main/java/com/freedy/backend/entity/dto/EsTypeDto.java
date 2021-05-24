package com.freedy.backend.entity.dto;

import com.freedy.backend.enumerate.EsType;
import lombok.Data;

/**
 * @author Freedy
 * @date 2021/5/3 15:15
 */
@Data
public class EsTypeDto {
    private EsType type;
    private Long id;
    private Long publishTime;



    //记录是否是顶置
    private boolean isOverHead;
}
