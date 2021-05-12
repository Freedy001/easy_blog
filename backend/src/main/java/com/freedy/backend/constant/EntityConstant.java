package com.freedy.backend.constant;

/**
 * @author Freedy
 * @date 2021/5/2 20:52
 */
public class EntityConstant {
    //文章状态 1:未发布 2:回收站 3:已发布 4:顶置 5:推荐
    public static final Integer ARTICLE_UNPUBLISHED=1;
    public static final Integer ARTICLE_RECYCLE=2;
    public static final Integer ARTICLE_PUBLISHED=3;
    public static final Integer ARTICLE_Overhead=4;
    public static final Integer ARTICLE_RECOMMEND=5;

    //文章评论 0:开启 1:不开启
    public static final Integer ARTICLE_CAN_COMMENT=0;
    public static final Integer ARTICLE_CAN_NOT_COMMENT=1;

    //优先级 1最高 2高 3普通 4低 5最低
    public static final Integer PRIORITY_HIGHEST =1;
    public static final Integer PRIORITY_HIGH=2;
    public static final Integer PRIORITY_NORMAL=3;
    public static final Integer PRIORITY_LOW=4;
    public static final Integer PRIORITY_LOWEST=5;

    //用户状态 1主管理员 2启用 3启用
    public static final Integer ROOT_ADMIN=1;
    public static final Integer USER_ENABLED=2;
    public static final Integer USER_DISABLED=3;



}
