package com.freedy.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.freedy.backend.common.utils.PageUtils;
import com.freedy.backend.entity.CommentEntity;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 评论表
 *
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
public interface CommentService extends IService<CommentEntity> {

    /**
     *  查询文章评论
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 发表文章
     */
    void publishComment(CommentEntity comment);

    /**
     * 后台查询文章评论
     */
    PageUtils queryAdminPage(Map<String, Object> params) throws ExecutionException, InterruptedException;

}

