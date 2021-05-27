package com.freedy.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.freedy.backend.utils.PageUtils;
import com.freedy.backend.entity.CommentEntity;

import java.util.List;
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
    PageUtils queryPage(Map<String, Object> params) throws ExecutionException, InterruptedException;

    /**
     * 发表文章
     */
    void publishComment(CommentEntity comment);

    /**
     * 后台查询文章评论
     */
    PageUtils queryAdminPage(Map<String, Object> params) throws ExecutionException, InterruptedException;

    /**
     * 后台管理员回复评论
     */
    void replay(CommentEntity commentEntity);

    /**
     * 批量删除评论
     */
    void deleteComment(List<Long> asList);

    /**
     * 批量审核通过
     */
    void confirmExaminations(List<Long> asList);


    /**
     * 获取每个文章的评论数
     * @return 键为文章id 值为数量
     */
    Map<String, String> getArticleCommentNum();

    /**
     * 根据文章id批量删除
     * @param articleId 文章id
     */
    void removeCommentByArticleIds(List<Long> articleId);

    /**
     * 获取指定用户未读的消息
     * @param id 用户id
     * @return
     */
    int countNotRead(Integer id);
}

