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

    /* 通知 */
    NOTIFY_INDEX_SETTING(205,"重新加载配置"),
    NOTIFY_ARTICLE_UPDATE(206,"文章更新"),

    /* 失败 */
    COMMON_FAIL(999, "失败"),
    UNKNOWN_EXCEPTION(500, "发生异常！"),
    LOGOUT_ERROR(501, "退出登录失败！"),
    METHOD_ERROR(502, "方法错误，不允许访问呢此方法！"),

    /* 参数错误：1000～1999 */
    PARAM_NOT_VALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),

    /* 用户错误 */
    USER_ACCOUNT_NOT_EXIST(2000, "账号不存在"),
    USER_NO_CERTIFICATE_OR_ACCOUNT_EXPIRED(2001, "无凭证信息或账号已过期！"),
    USER_CERTIFICATE_HAS_BEEN_CHANGED(2002, "用户凭证已被修改，请重新登录！"),
    OLD_PASSWORD_NOT_CORRECT(2003, "旧密码不正确！"),

    /* 文件错误 */
    FILE_IS_EMPTY(4000,"文件为空"),
    FILE_IS_NOT_SUIT(4001,"文件格式不支持"),
    UP_LOAD_FAILED(4002,"文件上传失败"),
    FILE_DELETE_ERROR(4003,"文件删除失败"),
    CAN_NOT_DEL_OTHER_ATTACHMENT(4004,"不能删除他人上传的照片"),

    /* 业务错误 */
    TAG_HAS_BEEN_USED(3000,"该值被其他地方所引用！"),
    NO_PERMISSION(3001, "没有权限"),
    NO_ARTICLE(3002, "没有找到文章"),
    NO_ID(3003, "没有id参数"),
    ARGUMENT_ERROR(3004, "参数不正确"),
    OUT_OF_LIMIT(3005, "字数超出限制"),
    VERIFY_ERROR(3006, "邮箱认证失败"),
    EMAIL_ALREADY_EXIST(3007, "邮箱已经存在"),
    NO_SMTP_CONFIG(3008, "博主没有配置smtp服务，无法订阅！"),
    LACK_VALUE(3009, "相关配置信息不全！");

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
