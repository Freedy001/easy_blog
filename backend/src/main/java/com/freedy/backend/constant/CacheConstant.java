package com.freedy.backend.constant;

/**
 * @author Freedy
 * @date 2021/5/17 23:38
 */
public class CacheConstant {
    //缓存前缀
    private static final String PREFIX="EASY-BLOG-CACHE:";
    //文章缓存名
    public static final String ARTICLE_CACHE_NAME=PREFIX+"article";
    //评论缓存名
    public static final String COMMENT_CACHE_NAME=PREFIX+"comment";
    //评论缓存名
    public static final String OPERATION_CACHE_NAME=PREFIX+"operation";
}
