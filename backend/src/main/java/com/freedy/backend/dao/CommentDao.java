package com.freedy.backend.dao;

import com.freedy.backend.entity.CommentEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
}
