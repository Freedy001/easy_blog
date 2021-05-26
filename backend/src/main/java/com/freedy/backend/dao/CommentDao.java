package com.freedy.backend.dao;

import com.freedy.backend.utils.PageUtils;
import com.freedy.backend.entity.CommentEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.freedy.backend.entity.vo.comment.CommentAdminVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 评论表
 * 
 * @author Freedy
 * @email 985948228@qq.com
 * @date 2021-04-25 14:00:46
 */
@Mapper
public interface CommentDao extends BaseMapper<CommentEntity> {

    /**
     * 获取指定文章最高的楼层
     */
    Integer getTopFlore(@Param("id") Long id);

    /**
     * 获取评论列表
     */
    List<CommentEntity> getCommentList(@Param("page") int page,
                                       @Param("limit") int limit,
                                       @Param("id") long id);

    /**
     *获取后台评论列表
     */
    List<CommentAdminVo> getAdminCommentList(PageUtils utils);

    /**
     * 获取自己文章的评论
     */
    List<CommentAdminVo> getOwnCommentList(@Param("utils") PageUtils utils, @Param("id") Integer id);

    /**
     * 获取同楼层的所有评论
     */
    List<CommentEntity> getAllCommentInOneFlore(List<CommentEntity> entities);

    /**
     * 审核通过
     */
    void confirmExaminations(List<Long> ids);

    /**
     * 读取所有评论
     */
    void readAll(List<String> ids);

    /**
     * 获取每个文章的评论数
     * @return 键为文章id 值为数量
     */
    List<Map<String, Long>> getArticleCommentNum();


    /**
     * 批量删除
     */
    void removeCommentByArticleIds(List<Long> articleId);

    /**
     * 获取指定文章的评论总数
     */
    Integer getCommentNumByArticleId(@Param("id") long id);
}
