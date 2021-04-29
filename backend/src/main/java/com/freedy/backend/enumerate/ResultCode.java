package com.freedy.backend.enumerate;

import com.freedy.backend.exception.CanNotFindCodeException;

/**
 * @author Freedy
 * @date 2021/4/27 20:14
 */
public enum ResultCode {
    /* 成功 */
    SUCCESS(200, "成功"),
    LOGIN_SUCCESS(201, "登录成功"),

    /* 默认失败 */
    COMMON_FAIL(999, "失败"),

    /* 参数错误：1000～1999 */
    PARAM_NOT_VALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),

    /* 用户错误 */
    USER_NO_CERTIFICATE(2000, "无凭证信息"),
    USER_NOT_LOGIN(2001, "用户未登录"),
    USER_ACCOUNT_EXPIRED(2002, "账号已过期"),
    USER_CREDENTIALS_ERROR(2003, "密码错误"),
    USER_CREDENTIALS_EXPIRED(2004, "密码过期"),
    USER_ACCOUNT_DISABLE(2005, "账号不可用"),
    USER_ACCOUNT_LOCKED(2006, "账号被锁定"),
    USER_ACCOUNT_NOT_EXIST(2007, "账号不存在"),
    USER_ACCOUNT_ALREADY_EXIST(2008, "账号已存在"),
    USER_ACCOUNT_USE_BY_OTHERS(2009, "账号下线"),


    /* 业务错误 */
    NO_PERMISSION(3001, "没有权限");
    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getStrCode() {
        return code+"";
    }


    public String getMessage() {
        return message;
    }


    /**
     * 根据code获取message
     */
    public static String getMessageByCode(Integer code) {
        for (ResultCode ele : values()) {
            if (ele.getCode().equals(code)) {
                return ele.getMessage();
            }
        }
        try {
            throw new CanNotFindCodeException("没有找到相应状态码!");
        } catch (CanNotFindCodeException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getMessageByCode(String code) {
        try {
            for (ResultCode ele : values()) {
                if (ele.getCode().equals(Integer.parseInt(code))) {
                    return ele.getMessage();
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }
}
