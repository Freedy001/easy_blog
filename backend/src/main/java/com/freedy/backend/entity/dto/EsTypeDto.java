package com.freedy.backend.entity.dto;

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

    public enum EsType {
        SAVE,
        UPDATE,
        DELETE
    }
}
