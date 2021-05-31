package com.freedy.backend.constant;

/**
 * @author Freedy
 * @date 2021/4/29 8:34
 */
public class RedisConstant {
    public static final String PREFIX = "EASY-BLOG";
    //用户token
    public static final String USER_TOKEN_HEADER = PREFIX + "user-token:";
    //心跳
    public static final String USER_IP_HEADER = PREFIX + "heartbeat:";
    //通知
    public static final String NOTIFY_HEADER = PREFIX + "notify";
    //订阅
    public static final String SUBSCRIBE_HEADER = PREFIX + "subscriber-varietyCode:";
    //喜欢参数
    public static final String ARTICLE_LIKE_HEADER = PREFIX + "article-like-parameter:";
    //评论参数
    public static final String ARTICLE_COMMENT_HEADER = PREFIX + "article-comment-parameter:";
    //拜访参数
    public static final String ARTICLE_VISIT_HEADER = PREFIX + "article-visit-parameter:";


}
