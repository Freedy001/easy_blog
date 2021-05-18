package com.freedy.backend.entity.vo;

import io.swagger.models.Operation;
import lombok.Data;

import javax.servlet.ServletRegistration.Dynamic;

/**
 * @author Freedy
 * @date 2021/5/18 22:55
 */
@Data
public class DashBoardVo {
    private Integer articleNum;
    private String commentNUm;
    private String visitNum;
    private String establishmentTime;
    private Operation dynamic;
    private Operation operationLog;

    @Data
    public static class Operation{
        private String operation;
        private String operator;
        private String operationTime;
        private String operationStatus;
    }
}
